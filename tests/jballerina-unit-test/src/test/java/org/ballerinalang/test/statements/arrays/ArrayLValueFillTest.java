/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.test.statements.arrays;

import io.ballerina.runtime.api.utils.StringUtils;
import io.ballerina.runtime.api.values.BArray;
import io.ballerina.runtime.api.values.BMap;
import io.ballerina.runtime.api.values.BObject;
import io.ballerina.runtime.internal.util.exceptions.BLangRuntimeException;
import org.ballerinalang.test.BCompileUtil;
import org.ballerinalang.test.CompileResult;
import org.ballerinalang.test.JvmRunUtil;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.ballerina.runtime.api.utils.TypeUtils.getType;
import static org.testng.Assert.assertEquals;

/**
 * Test cases for filling the elements of a multi-dimensional array when it is used as an LValue.
 *
 * @since 1.2.0
 */
public class ArrayLValueFillTest {

    private CompileResult compileResult;

    @BeforeClass
    public void setup() {
        compileResult = BCompileUtil.compile("test-src/statements/arrays/array_lvalue_fill_test.bal");
    }

    @Test
    public void testSimpleBasic2DArrays() {
        JvmRunUtil.invoke(compileResult, "testSimpleBasic2DArrays");
    }

    @Test
    public void testRecordArrays() {
        JvmRunUtil.invoke(compileResult, "testRecordArrays");
    }

    @Test
    public void test2DRecordArrays() {
        JvmRunUtil.invoke(compileResult, "test2DRecordArrays");
    }

    @Test
    public void testObjectArrays() {
        BArray arr = (BArray) JvmRunUtil.invoke(compileResult, "testObjectArrays");
        assertEquals(arr.size(), 2);

        BObject person = (BObject) arr.getRefValue(0);
        assertEquals(getType(person).getName(), "PersonObj");
        assertEquals(person.get(StringUtils.fromString("name")).toString(), "John Doe");

        person = (BObject) arr.getRefValue(1);
        assertEquals(getType(person).getName(), "PersonObj");
        assertEquals(person.get(StringUtils.fromString("name")).toString(), "Pubudu");
    }

    @Test
    public void test2DObjectArrays() {
        BArray arr = (BArray) JvmRunUtil.invoke(compileResult, "test2DObjectArrays");

        assertEquals(arr.size(), 3);
        assertEquals(((BArray) arr.getRefValue(0)).size(), 0);
        assertEquals(((BArray) arr.getRefValue(1)).size(), 0);
        assertEquals(((BArray) arr.getRefValue(2)).size(), 2);

        BArray peopleArr = (BArray) arr.getRefValue(2);
        for (int i = 0; i < peopleArr.size(); i++) {
            BObject person = (BObject) peopleArr.getRefValue(i);
            assertEquals(getType(person).getName(), "PersonObj");
            assertEquals(person.get(StringUtils.fromString("name")).toString(), "John Doe");
        }
    }

    // https://github.com/ballerina-platform/ballerina-lang/issues/20983
    @Test(enabled = false)
    public void test2DObjectArrays2() {
        BArray arr = (BArray) JvmRunUtil.invoke(compileResult, "test2DObjectArrays2");

        assertEquals(arr.size(), 3);
        assertEquals(((BArray) arr.getRefValue(0)).size(), 0);
        assertEquals(((BArray) arr.getRefValue(1)).size(), 0);
        assertEquals(((BArray) arr.getRefValue(2)).size(), 2);

        BArray peopleArr = (BArray) arr.getRefValue(2);
        for (int i = 0; i < peopleArr.size(); i++) {
            BMap person = (BMap) peopleArr.getRefValue(i);
            assertEquals(getType(person).getName(), "PersonObj");
            assertEquals(person.get(StringUtils.fromString("name")).toString(), "John Doe");
        }
    }

    @Test(expectedExceptions = BLangRuntimeException.class,
          expectedExceptionsMessageRegExp = "error: \\{ballerina/lang.array\\}IllegalListInsertion " +
                  "\\{\"message\":\"array of length 0 cannot be expanded into array of length 2 without " +
                  "filler values.*")
    public void test2DObjectArrays3() {
        JvmRunUtil.invoke(compileResult, "test2DObjectArrays3");
    }

    @Test(expectedExceptions = BLangRuntimeException.class,
          expectedExceptionsMessageRegExp = "error: \\{ballerina/lang.array\\}IllegalListInsertion " +
                  "\\{\"message\":\"array of length 0 cannot be expanded into array of length 2 without " +
                  "filler values.*")
    public void testRecordsWithoutFillerValues() {
        JvmRunUtil.invoke(compileResult, "testRecordsWithoutFillerValues");
    }

    @Test(expectedExceptions = BLangRuntimeException.class,
          expectedExceptionsMessageRegExp = "error: \\{ballerina/lang.array\\}IllegalListInsertion " +
                  "\\{\"message\":\"array of length 0 cannot be expanded into array of length 1 without " +
                  "filler values.*")
    public void testRecordsWithoutFillerValues2() {
        JvmRunUtil.invoke(compileResult, "testRecordsWithoutFillerValues2");
    }

    @Test
    public void testArraysInRecordFields() {
        JvmRunUtil.invoke(compileResult, "testArraysInRecordFields");
    }

    @Test
    public void testArraysInObjectFields() {
        JvmRunUtil.invoke(compileResult, "testArraysInObjectFields");
    }

    @Test
    public void testArraysInUnionTypes() {
        JvmRunUtil.invoke(compileResult, "testArraysInUnionTypes");
    }

    @Test
    public void testArraysOfTuples() {
        JvmRunUtil.invoke(compileResult, "testArraysOfTuples");
    }

    @Test
    public void test2DArrayInATuple() {
        JvmRunUtil.invoke(compileResult, "test2DArrayInATuple");
    }

    @Test
    public void testFiniteTyped2DArrays() {
        JvmRunUtil.invoke(compileResult, "testFiniteTyped2DArrays");
    }

    @Test(expectedExceptions = BLangRuntimeException.class,
          expectedExceptionsMessageRegExp = "error: \\{ballerina/lang.array\\}IllegalListInsertion " +
                  "\\{\"message\":\"array of length 0 cannot be expanded into array of length 2 without " +
                  "filler values.*")
    public void testNoDefFiniteTyped2DArrays() {
        JvmRunUtil.invoke(compileResult, "testNoDefFiniteTyped2DArrays");
    }

    @Test
    public void testMapArrayAsAnLValue() {
        JvmRunUtil.invoke(compileResult, "testMapArrayAsAnLValue");
    }

    @AfterClass
    public void tearDown() {
        compileResult = null;
    }
}
