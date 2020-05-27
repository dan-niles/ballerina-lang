/*
 * Copyright (c) 2019, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballerinalang.langserver.extensions.document;

import com.google.gson.JsonElement;
import org.ballerinalang.langserver.extensions.LSExtensionTestUtil;
import org.ballerinalang.langserver.extensions.ballerina.document.ASTModification;
import org.ballerinalang.langserver.extensions.ballerina.document.BallerinaASTResponse;
import org.ballerinalang.langserver.util.FileUtils;
import org.ballerinalang.langserver.util.TestUtil;
import org.eclipse.lsp4j.jsonrpc.Endpoint;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Test visible endpoint detection.
 */
public class ASTModifyTest {

    private static final String OS = System.getProperty("os.name").toLowerCase();
    private Endpoint serviceEndpoint;

    private Path mainFile = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify")
            .resolve("main.bal");

    private Path mainEmptyFile = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify")
            .resolve("mainEmpty.bal");

    private Path mainAccuweatherFile = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify")
            .resolve("mainAccuweather.bal");

    private Path mainTwilioFile = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify")
            .resolve("mainTwilio.bal");

    private Path emptyFile = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify")
            .resolve("empty.bal");

    private Path mainAccuweatherFile1 = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify")
            .resolve("mainAccuweather1.bal");

    private Path serviceAccuweatherFile1 = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify")
            .resolve("serviceAccuweather1.bal");

    private Path sourceRootPath = FileUtils.RES_DIR.resolve("extensions")
            .resolve("document")
            .resolve("ast")
            .resolve("modify");

    public static void skipOnWindows() {
        if (OS.contains("win")) {
            throw new SkipException("Skipping the test case on Windows");
        }
    }

    @BeforeClass
    public void startLangServer() throws IOException {
        this.serviceEndpoint = TestUtil.initializeLanguageSever();
    }

    private void assertTree(JsonElement actual, JsonElement expected) {
        if (expected.isJsonObject()) {
            Assert.assertEquals(expected.getAsJsonObject().keySet(), actual.getAsJsonObject().keySet());
            for (String key : expected.getAsJsonObject().keySet()) {
                if (!key.equals("id") && !key.equals("ws")) {
                    JsonElement expectedElement = expected.getAsJsonObject().get(key);
                    JsonElement actualElement = actual.getAsJsonObject().get(key);
                    assertTree(actualElement, expectedElement);
                }
            }
        } else if (expected.isJsonArray()) {
            Assert.assertEquals(expected.getAsJsonArray().size(), actual.getAsJsonArray().size());
            for (int i = 0; i < expected.getAsJsonArray().size(); i++) {
                assertTree(actual.getAsJsonArray().get(i), expected.getAsJsonArray().get(i));
            }
        } else if (expected.isJsonNull()) {
            Assert.assertTrue(actual.isJsonNull());
        } else if (expected.isJsonPrimitive()) {
            if (!expected.getAsString().contains(".bal")) {
                Assert.assertEquals(expected.getAsString(), actual.getAsString());
            }
        }
    }

    private Path createTempFile(Path filePath) throws IOException {
        Path tempFilePath = FileUtils.BUILD_DIR.resolve("tmp")
                .resolve(UUID.randomUUID() + ".bal");
        Files.copy(filePath, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
        return tempFilePath;
    }

    @Test(description = "Remove content.")
    public void testDelete() throws IOException {
        skipOnWindows();
        Path tempFile = createTempFile(mainFile);
        TestUtil.openDocument(serviceEndpoint, tempFile);
        ASTModification modification = new ASTModification(4, 5, 4, 33, "delete", null);
        BallerinaASTResponse astModifyResponse = LSExtensionTestUtil
                .modifyAndGetBallerinaAST(tempFile.toString(),
                        new ASTModification[]{modification}, this.serviceEndpoint);
        Assert.assertTrue(astModifyResponse.isParseSuccess());
        BallerinaASTResponse astResponse = LSExtensionTestUtil.getBallerinaDocumentAST(
                mainEmptyFile.toString(), this.serviceEndpoint);
        assertTree(astModifyResponse.getAst(), astResponse.getAst());
        Assert.assertEquals(astResponse.getSource(), mainEmptyFile.toString());
        TestUtil.closeDocument(this.serviceEndpoint, tempFile);
    }
//
//    @Test(description = "Insert content.")
//    public void testInsert() throws IOException {
//        TestUtil.openDocument(serviceEndpoint, mainFile);
//        Gson gson = new Gson();
//        ASTModification modification1 = new ASTModification(1, 1, 1, 1, "IMPORT",
//                gson.fromJson("{\"TYPE\":\"ballerina/accuweather\"}", JsonObject.class));
//        ASTModification modification2 = new ASTModification(4, 1, 4, 1, "DECLARATION",
//                gson.fromJson("{\"TYPE\":\"accuweather:Client\", \"VARIABLE\":\"accuweatherClient\"," +
//                        "\"PARAMS\": [\"\\\"8dbh68Zg2J6WxAK37Cy2jVJTSMdnyAmV\\\"\"]}", JsonObject.class));
//        ASTModification modification3 = new ASTModification(4, 1, 4, 1,
//                "REMOTE_SERVICE_CALL_CHECK",
//                gson.fromJson("{\"TYPE\":\"accuweather:WeatherResponse\", \"VARIABLE\":\"accuweatherResult\"," +
//                        "\"CALLER\":\"accuweatherClient\", \"FUNCTION\":\"getDailyWeather\"," +
//                        "\"PARAMS\": [\"\\\"80000\\\"\"]}", JsonObject.class));
//        BallerinaASTResponse astModifyResponse = LSExtensionTestUtil
//                .modifyAndGetBallerinaAST(mainFile.toString(),
//                        new ASTModification[]{modification1, modification2, modification3}, this.serviceEndpoint);
//        Assert.assertTrue(astModifyResponse.isParseSuccess());
//        BallerinaASTResponse astResponse = LSExtensionTestUtil.getBallerinaDocumentAST(
//                mainAccuweatherFile.toString(), this.serviceEndpoint);
//        assertTree(astModifyResponse.getAst(), astResponse.getAst());
//        TestUtil.closeDocument(this.serviceEndpoint, mainFile);
//    }
//
//    @Test(description = "Update content.")
//    public void testUpdate() throws IOException {
//        TestUtil.openDocument(serviceEndpoint, mainFile);
//        Gson gson = new Gson();
//        ASTModification modification1 = new ASTModification(1, 1, 1, 21, "IMPORT",
//                gson.fromJson("{\"TYPE\":\"ballerina/twilio\"}", JsonObject.class));
//        ASTModification modification2 = new ASTModification(4, 1, 4, 33, "DECLARATION",
//                gson.fromJson("{\"TYPE\":\"twilio:Client\", \"VARIABLE\":\"twilioClient\"," +
//                        "\"PARAMS\": [" +
//                        "   \"\\\"ACb2e9f049adcb98c7c31b913f8be70733\\\"\", " +
//                        "   \"\\\"34b2e025b2db33da04cc53ead8ce09bf\\\"\", " +
//                        "   \"\\\"\\\"\"]}", JsonObject.class));
//        ASTModification modification3 = new ASTModification(5, 1, 5, 1,
//                "REMOTE_SERVICE_CALL_CHECK",
//                gson.fromJson("{\"TYPE\":\"twilio:WhatsAppResponse\", \"VARIABLE\":\"twilioResult\"," +
//                                "\"CALLER\":\"twilioClient\", \"FUNCTION\":\"sendWhatsAppMessage\"," +
//                                "\"PARAMS\": [\"\\\"+14155238886\\\"\", \"\\\"+94773898282\\\"\", " +
//                                "\"dataMapperResult\"]}",
//                        JsonObject.class));
//        BallerinaASTResponse astModifyResponse = LSExtensionTestUtil
//                .modifyAndGetBallerinaAST(mainFile.toString(),
//                        new ASTModification[]{modification1, modification2, modification3}, this.serviceEndpoint);
//        Assert.assertTrue(astModifyResponse.isParseSuccess());
//
//        BallerinaASTResponse astResponse = LSExtensionTestUtil.getBallerinaDocumentAST(
//                mainTwilioFile.toString(), this.serviceEndpoint);
//        Assert.assertEquals(astModifyResponse.getAst(), astResponse.getAst());
//        TestUtil.closeDocument(this.serviceEndpoint, mainFile);
//    }
//
//    @Test(description = "Main content.")
//    public void testMain() throws IOException {
//        TestUtil.openDocument(serviceEndpoint, emptyFile);
//        Gson gson = new Gson();
//
//        ASTModification modification1 = new ASTModification(1, 1, 1, 1, "IMPORT",
//                gson.fromJson("{\"TYPE\":\"ballerina/accuweather\"}", JsonObject.class));
//        ASTModification modification2 = new ASTModification(1, 1, 1, 1, "MAIN_START",
//                gson.fromJson("{}", JsonObject.class));
//        ASTModification modification3 = new ASTModification(1, 1, 1, 1, "DECLARATION",
//                gson.fromJson("{\"TYPE\":\"accuweather:Client\", \"VARIABLE\":\"accuweatherClient\"," +
//                        "\"PARAMS\": [\"\\\"8dbh68Zg2J6WxAK37Cy2jVJTSMdnyAmV\\\"\"]}", JsonObject.class));
//        ASTModification modification4 = new ASTModification(1, 1, 1, 1,
//                "REMOTE_SERVICE_CALL_CHECK",
//                gson.fromJson("{\"TYPE\":\"accuweather:WeatherResponse\", \"VARIABLE\":\"accuweatherResult\"," +
//                        "\"CALLER\":\"accuweatherClient\", \"FUNCTION\":\"getDailyWeather\"," +
//                        "\"PARAMS\": [\"\\\"80000\\\"\"]}", JsonObject.class));
//        ASTModification modification5 = new ASTModification(1, 1, 1, 1, "MAIN_END",
//                gson.fromJson("{}", JsonObject.class));
//
//        BallerinaASTResponse astModifyResponse = LSExtensionTestUtil
//                .modifyAndGetBallerinaAST(emptyFile.toString(),
//                        new ASTModification[]{modification1, modification2, modification3, modification4,
//                                modification5}, this.serviceEndpoint);
//        Assert.assertTrue(astModifyResponse.isParseSuccess());
//
//        BallerinaASTResponse astResponse = LSExtensionTestUtil.getBallerinaDocumentAST(
//                mainAccuweatherFile1.toString(), this.serviceEndpoint);
//        Assert.assertEquals(astModifyResponse.getAst(), astResponse.getAst());
//        TestUtil.closeDocument(this.serviceEndpoint, emptyFile);
//    }
//
//    @Test(dependsOnMethods = "testMain", description = "Main content insert.")
//    public void testMainInsert() throws IOException {
//        TestUtil.openDocument(serviceEndpoint, emptyFile);
//        Gson gson = new Gson();
//
//        ASTModification modification1 = new ASTModification(1, 1, 1, 1, "MAIN_START",
//                gson.fromJson("{}", JsonObject.class));
//        ASTModification modification2 = new ASTModification(1, 1, 1, 1, "MAIN_END",
//                gson.fromJson("{}", JsonObject.class));
//
//        BallerinaASTResponse astModifyResponse = LSExtensionTestUtil
//                .modifyAndGetBallerinaAST(emptyFile.toString(),
//                        new ASTModification[]{modification1, modification2}, this.serviceEndpoint);
//        Assert.assertTrue(astModifyResponse.isParseSuccess());
//
//        ASTModification modification3 = new ASTModification(1, 1, 1, 1, "IMPORT",
//                gson.fromJson("{\"TYPE\":\"ballerina/accuweather\"}", JsonObject.class));
//        ASTModification modification4 = new ASTModification(2, 1, 2, 1, "DECLARATION",
//                gson.fromJson("{\"TYPE\":\"accuweather:Client\", \"VARIABLE\":\"accuweatherClient\"," +
//                        "\"PARAMS\": [\"\\\"8dbh68Zg2J6WxAK37Cy2jVJTSMdnyAmV\\\"\"]}", JsonObject.class));
//        ASTModification modification5 = new ASTModification(2, 1, 2, 1,
//                "REMOTE_SERVICE_CALL_CHECK",
//                gson.fromJson("{\"TYPE\":\"accuweather:WeatherResponse\", \"VARIABLE\":\"accuweatherResult\"," +
//                        "\"CALLER\":\"accuweatherClient\", \"FUNCTION\":\"getDailyWeather\"," +
//                        "\"PARAMS\": [\"\\\"80000\\\"\"]}", JsonObject.class));
//
//        BallerinaASTResponse astModifyResponse2 = LSExtensionTestUtil
//                .modifyAndGetBallerinaAST(emptyFile.toString(),
//                        new ASTModification[]{modification3, modification4, modification5}, this.serviceEndpoint);
//        Assert.assertTrue(astModifyResponse2.isParseSuccess());
//
//        BallerinaASTResponse astResponse = LSExtensionTestUtil.getBallerinaDocumentAST(
//                mainAccuweatherFile1.toString(), this.serviceEndpoint);
//        Assert.assertEquals(astModifyResponse2.getAst(), astResponse.getAst());
//        TestUtil.closeDocument(this.serviceEndpoint, emptyFile);
//    }
//
//    @Test(description = "Main to Service.")
//    public void testMainToService() throws IOException {
//        TestUtil.openDocument(serviceEndpoint, emptyFile);
//        Gson gson = new Gson();
//
//        ASTModification modification0 = new ASTModification(1, 1, 1, 1, "IMPORT",
//                gson.fromJson("{\"TYPE\":\"ballerina/http\"}", JsonObject.class));
//        ASTModification modification1 = new ASTModification(1, 1, 1, 1, "IMPORT",
//                gson.fromJson("{\"TYPE\":\"ballerina/accuweather\"}", JsonObject.class));
//        ASTModification modification2 = new ASTModification(1, 1, 1, 1, "SERVICE_START",
//                gson.fromJson("{\"SERVICE\":\"hello\", \"RESOURCE\":\"sayHello\", \"PORT\":\"9090\"}",
//                        JsonObject.class));
//        ASTModification modification3 = new ASTModification(1, 1, 1, 1, "DECLARATION",
//                gson.fromJson("{\"TYPE\":\"accuweather:Client\", \"VARIABLE\":\"accuweatherClient\"," +
//                        "\"PARAMS\": [\"\\\"8dbh68Zg2J6WxAK37Cy2jVJTSMdnyAmV\\\"\"]}", JsonObject.class));
//        ASTModification modification4 = new ASTModification(1, 1, 1, 1,
//                "REMOTE_SERVICE_CALL_CHECK",
//                gson.fromJson("{\"TYPE\":\"accuweather:WeatherResponse\", \"VARIABLE\":\"accuweatherResult\"," +
//                        "\"CALLER\":\"accuweatherClient\", \"FUNCTION\":\"getDailyWeather\"," +
//                        "\"PARAMS\": [\"\\\"80000\\\"\"]}", JsonObject.class));
//        ASTModification modification5 = new ASTModification(1, 1, 1, 1, "SERVICE_END",
//                gson.fromJson("{}", JsonObject.class));
//
//        BallerinaASTResponse astModifyResponse = LSExtensionTestUtil
//                .modifyAndGetBallerinaAST(emptyFile.toString(),
//                        new ASTModification[]{modification0, modification1, modification2, modification3,
//                                modification4, modification5}, this.serviceEndpoint);
//        Assert.assertTrue(astModifyResponse.isParseSuccess());
//
//        BallerinaASTResponse astResponse = LSExtensionTestUtil.getBallerinaDocumentAST(
//                serviceAccuweatherFile1.toString(), this.serviceEndpoint);
//        Assert.assertEquals(astModifyResponse.getAst(), astResponse.getAst());
//        TestUtil.closeDocument(this.serviceEndpoint, emptyFile);
//    }

//    @Test
//    public void testLookupPackageSourceForSinglePkg() {
//        CompilerContext context = new CompilerContext();
//        CompilerOptions options = CompilerOptions.getInstance(context);
//        options.put(PROJECT_DIR, sourceRootPath.toString());
//        options.put(SOURCE_TYPE, "SINGLE_BAL_FILE");
//        Compiler compiler = Compiler.getInstance(context);
//        BLangPackage bLangPackage = compiler.compile("main.bal");
//    }

    @AfterClass
    public void stopLangServer() {
        TestUtil.shutdownLanguageServer(this.serviceEndpoint);
    }
}
