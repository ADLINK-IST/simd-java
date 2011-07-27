/* Copyright 2010, Object Management Group, Inc.
 * Copyright 2010, PrismTech, Inc.
 * Copyright 2010, Real-Time Innovations, Inc.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.omg.dds.core.policy;

import org.omg.dds.type.BitBound;

public class TypeConsistencyEnforcement implements QosPolicy {
    // -- Constant Members
    private static final long serialVersionUID = 1L;
    public final static int ID = 18;
    private final static String NAME = "TypeConsistencyEnforcement";
    private static final TypeConsistencyEnforcement EXACT_TYPE =
            new TypeConsistencyEnforcement(Kind.EXACT_TYPE_TYPE_CONSISTENCY);
    private static final TypeConsistencyEnforcement EXACT_NAME =
            new TypeConsistencyEnforcement(Kind.EXACT_NAME_TYPE_CONSISTENCY);
    private static final TypeConsistencyEnforcement DECLARED =
            new TypeConsistencyEnforcement(Kind.DECLARED_TYPE_CONSISTENCY);
    private static final TypeConsistencyEnforcement ASSIGNABLE =
            new TypeConsistencyEnforcement(Kind.ASSIGNABLE_TYPE_CONSISTENCY);

    private Kind kind;

    private TypeConsistencyEnforcement(Kind kind) {
        this.kind = kind;
    }

    // -----------------------------------------------------------------------
    // Properties
    // -----------------------------------------------------------------------

    public Kind getKind() {
        return kind;
    }

    // -----------------------------------------------------------------------
    // Types
    // -----------------------------------------------------------------------

    @BitBound(16)
    public static enum Kind {
        EXACT_TYPE_TYPE_CONSISTENCY,
        EXACT_NAME_TYPE_CONSISTENCY,
        DECLARED_TYPE_CONSISTENCY,
        ASSIGNABLE_TYPE_CONSISTENCY
    }

    @Override
    public int getPolicyId() {
        return ID;
    }

    @Override
    public String getPolicyName() {
        return NAME;
    }

    public static TypeConsistencyEnforcement ExactType() {
        return EXACT_TYPE;
    }

    public static TypeConsistencyEnforcement ExactName() {
        return EXACT_NAME;
    }

    public static TypeConsistencyEnforcement Declared() {
        return DECLARED;
    }

    public static TypeConsistencyEnforcement Assignable() {
        return ASSIGNABLE;
    }
}
