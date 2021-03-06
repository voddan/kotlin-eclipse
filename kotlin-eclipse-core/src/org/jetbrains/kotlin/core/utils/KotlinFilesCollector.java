/*******************************************************************************
 * Copyright 2000-2014 JetBrains s.r.o.
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
 *
 *******************************************************************************/
package org.jetbrains.kotlin.core.utils;

import static org.eclipse.core.resources.ResourcesPlugin.getWorkspace;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.core.builder.KotlinPsiManager;
import org.jetbrains.kotlin.core.log.KotlinLogger;

public class KotlinFilesCollector {
    
    public static void collectForParsing() {
        try {
            new KotlinFilesCollector().addFilesToParse();
        } catch (CoreException e) {
            KotlinLogger.logError(e);
        }
    }
    
    private void addFilesToParse() throws CoreException {
        for (IProject project : getWorkspace().getRoot().getProjects()) {
            IJavaProject javaProject = JavaCore.create(project);
            for (IResource resource : project.members(false)) {
                scanForFiles(resource, javaProject);
            }
        }
    }
    
    private void scanForFiles(@NotNull IResource parentResource, @NotNull IJavaProject javaProject) throws CoreException {
        if (KotlinPsiManager.INSTANCE.isKotlinSourceFile(parentResource, javaProject)) {
            KotlinPsiManager.INSTANCE.updateProjectPsiSources((IFile) parentResource, IResourceDelta.ADDED);
            return; 
        }
        if (parentResource.getType() != IResource.FOLDER) {
            return;
        }
        IResource[] resources = ((IFolder) parentResource).members();
        for (IResource resource : resources) {
            scanForFiles(resource, javaProject);
        }
    }

}
