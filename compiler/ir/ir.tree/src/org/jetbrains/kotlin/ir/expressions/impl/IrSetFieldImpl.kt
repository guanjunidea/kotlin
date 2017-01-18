/*
 * Copyright 2010-2016 JetBrains s.r.o.
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

package org.jetbrains.kotlin.ir.expressions.impl

import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.PropertyDescriptor
import org.jetbrains.kotlin.ir.*
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.IrSetField
import org.jetbrains.kotlin.ir.expressions.impl.IrFieldExpressionBase
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer
import org.jetbrains.kotlin.ir.visitors.IrElementVisitor
import org.jetbrains.kotlin.types.typeUtil.builtIns

class IrSetFieldImpl(
        startOffset: Int, endOffset: Int,
        descriptor: PropertyDescriptor,
        origin: IrStatementOrigin? = null,
        superQualifier: ClassDescriptor? = null
) : IrFieldExpressionBase(startOffset, endOffset, descriptor, descriptor.type.builtIns.unitType, origin, superQualifier), IrSetField {
    constructor(
            startOffset: Int, endOffset: Int, descriptor: PropertyDescriptor,
            receiver: IrExpression?,
            value: IrExpression,
            origin: IrStatementOrigin? = null,
            superQualifier: ClassDescriptor? = null
    ) : this(startOffset, endOffset, descriptor, origin, superQualifier) {
        this.receiver = receiver
        this.value = value
    }

    override lateinit var value: IrExpression

    override fun <R, D> accept(visitor: IrElementVisitor<R, D>, data: D): R {
        return visitor.visitSetField(this, data)
    }

    override fun <D> acceptChildren(visitor: IrElementVisitor<Unit, D>, data: D) {
        receiver?.accept(visitor, data)
        value.accept(visitor, data)
    }

    override fun <D> transformChildren(transformer: IrElementTransformer<D>, data: D) {
        receiver = receiver?.transform(transformer, data)
        value = value.transform(transformer, data)
    }
}