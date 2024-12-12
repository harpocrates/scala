/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc. dba Akka
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.tools.ant.sabbus

import java.io.File
import org.apache.tools.ant.Task
import org.apache.tools.ant.types.{Path, Reference}
import org.apache.tools.ant.types.Commandline.Argument

trait CompilationPathProperty {
  this: Task =>

  protected var compilationPath: Option[Path] = None

  def setCompilationPath(input: Path) {
    if (compilationPath.isEmpty) compilationPath = Some(input)
    else compilationPath.get.append(input)
  }

  def createCompilationPath: Path = {
    if (compilationPath.isEmpty) compilationPath = Some(new Path(getProject()))
    compilationPath.get.createPath()
  }

  def setCompilationPathRef(input: Reference) {
    createCompilationPath.setRefid(input)
  }
}

trait TaskArgs extends CompilationPathProperty {
  this: Task =>

  def setId(input: String) {
    id = Some(input)
  }

  def setParams(input: String) {
    extraArgs ++= input.split(' ').map { s => val a = new Argument; a.setValue(s); a }
  }

  def createCompilerArg(): Argument = {
    val a = new Argument
    extraArgs :+= a
    a
  }

  def setTarget(input: String) {
    compTarget = Some(input)
  }

  def setSrcPath(input: Path) {
    if (sourcePath.isEmpty) sourcePath = Some(input)
    else sourcePath.get.append(input)
  }

  def createSrcPath: Path = {
    if (sourcePath.isEmpty) sourcePath = Some(new Path(getProject()))
    sourcePath.get.createPath()
  }

  def setSrcPathRef(input: Reference) {
    createSrcPath.setRefid(input)
  }

  def setCompilerPath(input: Path) {
    if (compilerPath.isEmpty) compilerPath = Some(input)
    else compilerPath.get.append(input)
  }

  def createCompilerPath: Path = {
    if (compilerPath.isEmpty) compilerPath = Some(new Path(getProject()))
    compilerPath.get.createPath()
  }

  def setCompilerPathRef(input: Reference) {
    createCompilerPath.setRefid(input)
  }

  def setDestdir(input: File) {
    destinationDir = Some(input)
  }

  protected var id: Option[String] = None
  protected var extraArgs: Seq[Argument] = Seq()
  protected var compTarget: Option[String] = None
  protected var sourcePath: Option[Path] = None
  protected var compilerPath: Option[Path] = None
  protected var destinationDir: Option[File] = None

  def extraArgsFlat: Seq[String] = extraArgs flatMap { a =>
    val parts = a.getParts
    if(parts eq null) Seq[String]() else parts.toSeq
  }
}
