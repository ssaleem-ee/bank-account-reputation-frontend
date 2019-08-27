/*
 * Copyright 2019 HM Revenue & Customs
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

package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class AccountDetails(account: Account)

case class Account(sortCode: String,
                   accountNumber: String)

case class EiscdCountry(name: String)

case class EiscdAddress(lines: Seq[String],
                        town: Option[String],
                        county: Option[String],
                        country: Option[EiscdCountry],
                        postCode: Option[String],
                        zipCode: Option[String])

object BacsStatus extends Enumeration {

  type BacsStatus = Value

  protected case class Val(status: String) extends super.Val

  import scala.language.implicitConversions

  implicit def valueToBacsStatusVal(x: Value): Val = x.asInstanceOf[Val]

  val NA = Val("!!! Unknown Bacs Office Status !!!")

  val M = Val("The bank office of a Bacs member; accepts Bacs payments")

  val A = Val("The bank office of an agency bank; accepts Bacs payments")

  val N = Val("he bank office does not accept Bacs payments. See field 17.")
}

object TransactionType extends Enumeration {

  type TransactionType = Value

  protected case class Val(name: String) extends super.Val

  import scala.language.implicitConversions

  implicit def valueToTransactionTypeVal(x: Value): Val = x.asInstanceOf[Val]

  val NA = Val("!!! Unknown Transaction Type !!!")

  val DR = Val("Direct Debits")

  val CR = Val("Direct Credits")

  val CU = Val("Claims for unpaid cheques")

  val PR = Val("Life office debit")

  val BS = Val("Building society credits")

  val DV = Val("Dividend interest payments")

  val AU = Val("Direct Debit instructions")
}

import models.BacsStatus.BacsStatus
import models.TransactionType._

case class EiscdEntry(bankCode: String,
                      bankName: String,
                      address: EiscdAddress,
                      telephone: Option[String],
                      bacsOfficeStatus: BacsStatus,
                      branchName: Option[String] = None,
                      ddiVoucherFlag: Option[String] = None,
                      disallowedTransactions: Seq[TransactionType] = Seq.empty)

case class Address(lines: List[String],
                   town: Option[String],
                   postcode: Option[String])

case class Subject(title: Option[String],
                   name: Option[String],
                   firstName: Option[String],
                   lastName: Option[String],
                   dob: Option[String],
                   address: Address)

case class Input(account: Account,
                 subject: Subject)

case class ModCheckResult(accountNumberWithSortCodeIsValid: Boolean,
                          nonStandardAccountDetailsRequiredForBacs: String)

case class ValidationResult(accountNumberWithSortCodeIsValid: Boolean,
                            nonStandardAccountDetailsRequiredForBacs: String,
                            sortCodeIsPresentOnEISCD: String,
                            supportsBACS: Option[String] = None,
                            ddiVoucherFlag: Option[String] = None,
                            directDebitsDisallowed: Option[String] = None,
                            directDebitInstructionsDisallowed: Option[String] = None)

case class Assessment(accountNumberWithSortCodeIsValid: Boolean,
                      accountExists: String,
                      nameMatches: String,
                      addressMatches: String,
                      nonConsented: String,
                      subjectHasDeceased: String,
                      nonStandardAccountDetailsRequiredForBacs: Option[String] = None)

case class AccountForm(sortCode: String,
                       accountNumber: String,
                       csrfToken: String)

case class SortCodeForm(sortCode: String,
                        csrfToken: String)

case class InputForm(input: Input,
                     csrfToken: String)

object Implicits {

  implicit val optionStringFormat = play.api.libs.json.Format.optionWithNull[String]

  implicit val accountFormat = Json.format[Account]

  implicit val accountDetailsFormat = Json.format[AccountDetails]

  implicit val eiscdCountryFormat = Json.format[EiscdCountry]

  implicit val eiscdAddressFormat = Json.format[EiscdAddress]

  implicit val addressFormat = Json.format[Address]

  implicit val subjectFormat = Json.format[Subject]

  implicit val inputFormat = Json.format[Input]

  implicit val validationResultFormat = Json.format[ValidationResult]

  implicit val modcheckResultFormat = Json.format[ModCheckResult]

  implicit val assessmentFormat = Json.format[Assessment]

  implicit def bacsOfficeStatus(statusCode: String): BacsStatus = BacsStatus.values.find(_.toString.matches(statusCode)).getOrElse(BacsStatus.NA)

  implicit def transactionType(transactionTypeCode: String): TransactionType = TransactionType.values.find(_.toString.matches(transactionTypeCode)).getOrElse(TransactionType.NA)

  implicit def transactionTypes(transactionTypeCodes: Option[Seq[String]]): Seq[TransactionType] = transactionTypeCodes.getOrElse(Seq.empty).map(code => transactionType(code))

  implicit val eiscdReads: Reads[EiscdEntry] =
    ((JsPath \ "bankCode").read[String] and
      (JsPath \ "bankName").read[String] and
      (JsPath \ "address").read[EiscdAddress] and
      (JsPath \ "telephone").readNullable[String] and
      (JsPath \ "bacsOfficeStatus").read[String].map(bacsOfficeStatus _) and
      (JsPath \ "branchName").readNullable[String] and
      (JsPath \ "ddiVoucherFlag").readNullable[String] and
      (JsPath \ "disallowedTransactions").readNullable[Seq[String]].map(transactionTypes _)
      ) (EiscdEntry.apply _)

  def opt(str: String): Option[String] = str.isEmpty match {
    case true => None
    case false => Some(str)
  }
}
