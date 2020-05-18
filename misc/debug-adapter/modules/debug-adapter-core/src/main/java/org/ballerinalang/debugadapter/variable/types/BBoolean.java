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

package org.ballerinalang.debugadapter.variable.types;

import com.sun.jdi.Value;
import com.sun.tools.jdi.BooleanValueImpl;
import org.ballerinalang.debugadapter.variable.BPrimitiveVariable;
import org.ballerinalang.debugadapter.variable.BVariableType;
import org.eclipse.lsp4j.debug.Variable;

/**
 * Ballerina boolean variable type.
 */
public class BBoolean extends BPrimitiveVariable {

    private final BooleanValueImpl jvmValueRef;

    public BBoolean(Value value, Variable dapVariable) {
        this.jvmValueRef = value instanceof BooleanValueImpl ? (BooleanValueImpl) value : null;
        dapVariable.setType(BVariableType.BOOLEAN.getString());
        dapVariable.setValue(this.getValue());
        this.setDapVariable(dapVariable);
    }

    @Override
    public String getValue() {
        return jvmValueRef != null ? jvmValueRef.toString() : "unknown";
    }
}
