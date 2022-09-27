// Copyright (c) 2022, WSO2 LLC. (https://www.wso2.com) All Rights Reserved.
//
// WSO2 LLC. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import configTableArray.type_defs;
import configTableArray.imported_tables;
import testOrg/configLib.util;
import ballerina/test;

configurable type_defs:PersonTable[] & readonly personTableArray = ?;
configurable (type_defs:PersonTable & readonly)[] personTableArray1 = ?;

type PersonTable table<type_defs:Person> key(id);

configurable PersonTable[] personTableArray2 = ?;

type MapTable table<map<anydata>>;

configurable MapTable[] mapTableArray1 = ?;
configurable table<map<int>>[] mapTableArray2 = ?;

public function testArrays() {
    test:assertEquals(personTableArray.toString(), "[[{\"name\":\"manu\",\"id\":11," +
    "\"address\":{\"city\":\"New York\",\"country\":{\"name\":\"USA\"}}},{\"name\":\"hinduja\",\"id\":12," +
    "\"address\":{\"city\":\"London\",\"country\":{\"name\":\"UK\"}}}],[{\"name\":\"waruna\",\"id\":700," +
    "\"address\":{\"city\":\"Abu Dhabi\",\"country\":{\"name\":\"UAE\"}}},{\"name\":\"manu\",\"id\":701," +
    "\"address\":{\"city\":\"Mumbai\",\"country\":{\"name\":\"India\"}}}]]");
    test:assertEquals(personTableArray1.toString(), "[[{\"name\":\"waruna\",\"id\":700," +
    "\"address\":{\"city\":\"Abu Dhabi\",\"country\":{\"name\":\"UAE\"}}},{\"name\":\"manu\",\"id\":701," +
    "\"address\":{\"city\":\"Mumbai\",\"country\":{\"name\":\"India\"}}}],[{\"name\":\"gabilan\",\"id\":900," +
    "\"address\":{\"city\":\"Abu Dhabi\",\"country\":{\"name\":\"UAE\"}}},{\"name\":\"hinduja\",\"id\":901," +
    "\"address\":{\"city\":\"Mumbai\",\"country\":{\"name\":\"India\"}}}]]");
    test:assertEquals(personTableArray2.toString(), "[[{\"name\":\"gabilan\",\"id\":900," +
    "\"address\":{\"city\":\"Abu Dhabi\",\"country\":{\"name\":\"UAE\"}}},{\"name\":\"hinduja\",\"id\":901," +
    "\"address\":{\"city\":\"Mumbai\",\"country\":{\"name\":\"India\"}}}],[{\"name\":\"manu\",\"id\":11,\"" +
    "address\":{\"city\":\"New York\",\"country\":{\"name\":\"USA\"}}},{\"name\":\"hinduja\",\"id\":12," +
    "\"address\":{\"city\":\"London\",\"country\":{\"name\":\"UK\"}}}]]");
    test:assertEquals(mapTableArray1.toString(), "[[{\"a\":1,\"b\":\"b\",\"c\":[1,\"2\",{\"c\":\"c\"}]," +
    "\"d\":{\"d\":\"D\"}},{\"e\":3,\"f\":\"f\",\"g\":[4,\"5\",{\"g\":\"g\"}],\"h\":{\"h\":\"H\"}}]," +
    "[{\"e\":5,\"f\":6}]]");
    test:assertEquals(mapTableArray2.toString(), "[[{\"a\":1,\"b\":2},{\"c\":3,\"d\":4}],[{\"e\":5,\"f\":6}]]");
}

public function main() {
    testArrays();
    testArrayIteration();
    imported_tables:testArrays();
    imported_tables:testArrayIteration();
    util:print("Tests passed");
}

function testArrayIteration() {
    util:testArrayIterator(personTableArray, 2);
    util:testArrayIterator(personTableArray1, 2);
    util:testArrayIterator(personTableArray2, 2);
    util:testArrayIterator(mapTableArray1, 2);
    util:testArrayIterator(mapTableArray2, 2);
}
