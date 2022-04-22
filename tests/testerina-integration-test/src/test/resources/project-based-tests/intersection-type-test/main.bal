// Copyright (c) 2021 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import ballerina/lang.'int as ints;

public type Uuid readonly & record {
    ints:Unsigned32 timeLow;
    ints:Unsigned16 timeMid;
    ints:Unsigned16 timeHiAndVersion;
    ints:Unsigned8 clockSeqHiAndReserved;
    ints:Unsigned8 clockSeqLo;
    int node;
};

public type RecordType record {
    int id;
};

type ImmutableRecordType readonly & RecordType;

public type ObjectType object {
    RecordType rec;
    Uuid uuid;
};

public type ObjectOrRecordType RecordType|ObjectType;

public type TupleType [int, string];

public type ArrayType RecordType[];

public function getUuid() returns Uuid {
    Uuid uuid = {timeLow: 1, timeMid: 2, timeHiAndVersion: 3, clockSeqHiAndReserved: 4, clockSeqLo: 5, node: 6};
    return uuid;
}

public function getRecordValue() returns ImmutableRecordType {
    return {id: 1};
}
