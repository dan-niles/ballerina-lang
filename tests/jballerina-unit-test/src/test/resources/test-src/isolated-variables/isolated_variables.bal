// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
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

int[] a = getIntArray();

isolated int[][] b = [a.cloneReadOnly(), getIntArray()];

isolated record {
    int[] i;
    boolean[] j;
} c = {
    i: x,
    j: getBooleanArray(<int[2]> getIntArray())
};

isolated object {
    int[] i;
    string[] j;
} d = object {
    int[] i = getIntArray();
    string[] j;

    isolated function init() {
        self.j = [];
    }
};

isolated NonIsolatedObject e = new NonIsolatedClassWithIsolatedInit(getIntArray(), ["hello", "world"]);

final readonly & int[] x = [1, 2, 3];

isolated function getIntArray() returns int[] => x;

type NonIsolatedObject object {
    int[] i;
    string[] j;
};

class NonIsolatedClassWithIsolatedInit {
    int[] i;
    string[] j;

    isolated function init(int[] i, string[] j) {
        self.i = i;
        self.j = j;
    }
}

isolated function getBooleanArray(int[] arr) returns boolean[] {
    return <boolean[]> arr.'map(intVal => intVal < 0);
}
