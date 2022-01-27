/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.langlib.test;

import io.ballerina.runtime.api.values.BArray;
import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.CompileResult;
import org.ballerinalang.test.JvmRunUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test cases for the lang.float library.
 *
 * @since 1.0
 */
public class LangLibFloatTest {

    private CompileResult compileResult;

    @BeforeClass
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/floatlib_test.bal");
    }

    @Test
    public void testIsFinite() {
        Object[] returns = JvmRunUtil.invoke(compileResult, "testIsFinite");
        BArray result = (BArray) returns[0];
        assertTrue(result.getBoolean(0));
        assertFalse(result.getBoolean(1));
    }

    @Test
    public void testIsInfinite() {
        Object[] returns = JvmRunUtil.invoke(compileResult, "testIsInfinite");
        BArray result = (BArray) returns[0];
        assertFalse(result.getBoolean(0));
        assertTrue(result.getBoolean(1));
    }

    @Test
    public void testSum() {

        Object[] returns = JvmRunUtil.invoke(compileResult, "testSum");
        assertEquals(returns[0], 70.35d);
    }

    @Test
    public void testFloatConsts() {

        Object[] returns = JvmRunUtil.invoke(compileResult, "testFloatConsts");
        BArray result = (BArray) returns[0];
        assertEquals(result.getFloat(0), Double.NaN);
        assertEquals(result.getFloat(1), Double.POSITIVE_INFINITY);
    }

    @Test
    public void testLangLibCallOnFiniteType() {
        JvmRunUtil.invoke(compileResult, "testLangLibCallOnFiniteType");
    }

    @Test(dataProvider = "functionsWithFloatEqualityChecks")
    public void testFunctionsWithFloatEqualityChecks(String function) {
        JvmRunUtil.invoke(compileResult, function);
    }

    @DataProvider
    public  Object[] functionsWithFloatEqualityChecks() {
        return new String[] {
                "testFloatEquality",
                "testFloatNotEquality",
                "testFloatExactEquality",
                "testFloatNotExactEquality"
        };
    }

    @Test
    public void testFromHexString() {
        JvmRunUtil.invoke(compileResult, "testFromHexString");
    }

    @Test
    public void testMinAndMaxWithNaN() {
        JvmRunUtil.invoke(compileResult, "testMinAndMaxWithNaN");
    }

    @Test(dataProvider = "functionsWithFromStringTests")
    public void testFromString(String function) {
        JvmRunUtil.invoke(compileResult, function);
    }

    @DataProvider
    public  Object[] functionsWithFromStringTests() {
        return new String[] {
                "testFromStringPositive",
                "testFromStringNegative"
        };
    }  
}
