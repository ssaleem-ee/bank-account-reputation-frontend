/*
 * Copyright 2021 HM Revenue & Customs
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

package util

sealed trait Feature {
  val name: String
}

abstract class FeatureBase(override val name: String) extends Feature

case object GetHelpWithThisPageNewWordingOfEntryLink extends FeatureBase("GetHelpWithThisPageNewWordingOfEntryLink")
case object GetHelpWithThisPageMoreVerboseHeader extends FeatureBase("GetHelpWithThisPageMoreVerboseHeader")
case object GetHelpWithThisPageMoreVerboseConfirmation extends FeatureBase("GetHelpWithThisPageMoreVerboseConfirmation")
case object GetHelpWithThisPageImprovedFieldValidation extends FeatureBase("GetHelpWithThisPageImprovedFieldValidation")
case object GetHelpWithThisPageNewLargeInputFields extends FeatureBase("GetHelpWithThisPageNewLargeInputFields")
case object GetHelpWithThisPageFeatureFieldHints extends FeatureBase("GetHelpWithThisPageFeatureFieldHints")
case object GetHelpWithThisPageOnlyServerSideValidation
    extends FeatureBase("GetHelpWithThisPageOnlyServerSideValidation")

object Feature {

  val byName: PartialFunction[String, Feature] = {
    case GetHelpWithThisPageMoreVerboseHeader.name        => GetHelpWithThisPageMoreVerboseHeader
    case GetHelpWithThisPageMoreVerboseConfirmation.name  => GetHelpWithThisPageMoreVerboseConfirmation
    case GetHelpWithThisPageImprovedFieldValidation.name  => GetHelpWithThisPageImprovedFieldValidation
    case GetHelpWithThisPageNewLargeInputFields.name      => GetHelpWithThisPageNewLargeInputFields
    case GetHelpWithThisPageFeatureFieldHints.name        => GetHelpWithThisPageFeatureFieldHints
    case GetHelpWithThisPageNewWordingOfEntryLink.name    => GetHelpWithThisPageNewWordingOfEntryLink
    case GetHelpWithThisPageOnlyServerSideValidation.name => GetHelpWithThisPageOnlyServerSideValidation
  }

}
