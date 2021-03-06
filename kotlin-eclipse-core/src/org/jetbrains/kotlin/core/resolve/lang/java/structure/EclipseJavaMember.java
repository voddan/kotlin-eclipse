/*******************************************************************************
 * Copyright 2000-2014 JetBrains s.r.o.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/
package org.jetbrains.kotlin.core.resolve.lang.java.structure;

import static org.jetbrains.kotlin.core.resolve.lang.java.structure.EclipseJavaElementFactory.annotations;

import java.lang.reflect.Modifier;
import java.util.Collection;

import org.eclipse.jdt.core.dom.IBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.descriptors.Visibility;
import org.jetbrains.kotlin.load.java.structure.JavaAnnotation;
import org.jetbrains.kotlin.load.java.structure.JavaMember;
import org.jetbrains.kotlin.name.FqName;
import org.jetbrains.kotlin.name.Name;

public abstract class EclipseJavaMember<T extends IBinding> extends EclipseJavaElement<T> implements JavaMember {

    protected EclipseJavaMember(@NotNull T javaElement) {
        super(javaElement);
    }
    
    @Override
    @NotNull
    public Collection<JavaAnnotation> getAnnotations() {
        return annotations(getBinding().getAnnotations());
    }

    @Override
    @Nullable
    public JavaAnnotation findAnnotation(@NotNull FqName fqName) {
        return EclipseJavaElementUtil.findAnnotation(getBinding().getAnnotations(), fqName);
    }

    @Override
    public boolean isAbstract() {
        return Modifier.isAbstract(getBinding().getModifiers());
    }

    @Override
    public boolean isStatic() {
        return Modifier.isStatic(getBinding().getModifiers());
    }

    @Override
    public boolean isFinal() {
        return Modifier.isFinal(getBinding().getModifiers());
    }

    @Override
    @NotNull
    public Visibility getVisibility() {
        return EclipseJavaElementUtil.getVisibility(getBinding());
    }

    @Override
    @NotNull
    public Name getName() {
        return Name.guess(getBinding().getName());
    }
}
