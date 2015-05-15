package models

import com.github.tototoshi.slick.JdbcJodaSupport._
import org.joda.time.DateTime

import securesocial.core.AuthenticationMethod
import securesocial.core.BasicProfile
import securesocial.core.UserProfile
import securesocial.core.OAuth1Info
import securesocial.core.OAuth2Info
import securesocial.core.PasswordInfo
import securesocial.core.providers.MailToken

import scala.slick.ast.ColumnOption.DBType
import scala.slick.driver.JdbcDriver.simple._
import scala.slick.lifted.TableQuery

import UserTableQueries._ 

import play.Logger

case class BasicUser(main: BasicProfile, identities: List[BasicProfile])

case class User(id: String, mainId: Long) {
  def basicUser(implicit session: Session): BasicUser = {
    val main = profiles.filter(_.id === mainId).first
    val identities = profiles.filter(p => p.userId === id && p.id =!= mainId).list

    BasicUser(main.basicProfile, identities.map(i => i.basicProfile))
  }
}

class Users(tag: Tag) extends Table[User](tag, "USER") {
  def id = column[String]("ID", O.PrimaryKey)
  def mainId = column[Long]("MAIN_ID")

  def * = (id, mainId) <> (User.tupled, User.unapply)
}

case class OAuth1(id: Option[Long] = None, token: String, secret: String) {
  def oAuth1Info: OAuth1Info = {
    OAuth1Info(token, secret)
  }
}

class OAuth1s(tag: Tag) extends Table[OAuth1](tag, "OAUTH1") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def token = column[String]("TOKEN")
  def secret = column[String]("SECRET")

  def * = (id.?, token, secret) <> (OAuth1.tupled, OAuth1.unapply)
}

case class OAuth2(id: Option[Long] = None, accessToken: String, tokenType: Option[String] = None,
                           expiresIn: Option[Int] = None, refreshToken: Option[String] = None) {
  def oAuth2Info: OAuth2Info = {
    OAuth2Info(accessToken, tokenType, expiresIn, refreshToken)
  }
}

class OAuth2s(tag: Tag) extends Table[OAuth2](tag, "OAUTH2") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def accessToken = column[String]("ACCESS_TOKEN")
  def tokenType = column[Option[String]]("TOKEN_TYPE")
  def expiresIn = column[Option[Int]]("EXPIRES_IN")
  def refreshToken = column[Option[String]]("REFRESH_TOKEN")

  def * = (id.?, accessToken, tokenType, expiresIn, refreshToken) <> (OAuth2.tupled, OAuth2.unapply)
}

case class Password(id: Option[Long] = None, hasher: String, password: String, salt: Option[String] = None) {
  def passwordInfo: PasswordInfo = {
    PasswordInfo(hasher, password, salt)
  }
}

class Passwords(tag: Tag) extends Table[Password](tag, "PASSWORD") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def hasher = column[String]("HASHER")
  def password = column[String]("PASSWORD")
  def salt = column[Option[String]]("SALT")

  def * = (id.?, hasher, password, salt) <> (Password.tupled, Password.unapply)
}

class MailTokens(tag: Tag) extends Table[MailToken](tag, "MAIL_TOKEN") {
  def uuid = column[String]("UUID", O.PrimaryKey)
  def email = column[String]("EMAIL")
  def creationTime = column[DateTime]("CREATION_TIME")
  def expirationTime = column[DateTime]("EXPIRATION_TIME")
  def isSignUp = column[Boolean]("IS_SIGN_UP")

  def * = (uuid, email, creationTime, expirationTime, isSignUp) <> (MailToken.tupled, MailToken.unapply)
}

case class UserAuthenticator(id: String, userId: String, expirationDate: DateTime, lastUsed: DateTime, creationDate: DateTime)

class UserAuthenticators(tag: Tag) extends Table[UserAuthenticator](tag, "AUTHENTICATOR") {
  def id = column[String]("ID", DBType("varchar(2000)"), O.PrimaryKey)
  def userId = column[String]("USER_ID")
  def expirationDate = column[DateTime]("EXPIRATION_DATE")
  def lastUsed = column[DateTime]("LAST_USED")
  def creationDate = column[DateTime]("CREATION_DATE")

  def * = (id, userId, expirationDate, lastUsed, creationDate) <> (UserAuthenticator.tupled, UserAuthenticator.unapply)
}

case class Profile(id: Option[Long] = None,
                        providerId: String,
                        userId: String,
                        firstName: Option[String] = None,
                        lastName: Option[String] = None,
                        fullName: Option[String] = None,
                        email: Option[String] = None,
                        avatarUrl: Option[String] = None,
                        authMethod: String,
                        oAuth1Id: Option[Long] = None,
                        oAuth2Id: Option[Long] = None,
                        passwordId: Option[Long] = None) extends UserProfile {
  def basicProfile(implicit session: Session): BasicProfile = {
    BasicProfile(
      providerId,
      userId,
      firstName,
      lastName,
      fullName,
      email,
      avatarUrl,
      authMethod match {
        case "oauth1" => AuthenticationMethod.OAuth1
        case "oauth2" => AuthenticationMethod.OAuth2
        case "openId" => AuthenticationMethod.OpenId
        case "userPassword" => AuthenticationMethod.UserPassword
      },
      oauth1s.filter(_.id === oAuth1Id).firstOption.map(o1 => o1.oAuth1Info),
      oauth2s.filter(_.id === oAuth2Id).firstOption.map(o2 => o2.oAuth2Info),
      passwords.filter(_.id === passwordId).firstOption.map(p => p.passwordInfo)
    )
  }
}

class Profiles(tag: Tag) extends Table[Profile](tag, "PROFILE") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def providerId = column[String]("PROVIDER_ID")
  def userId = column[String]("USER_ID")
  def firstName = column[Option[String]]("FIRST_NAME")
  def lastName = column[Option[String]]("LAST_NAME")
  def fullName = column[Option[String]]("FULL_NAME")
  def email = column[Option[String]]("EMAIL")
  def avatarUrl = column[Option[String]]("AVATAR_URL")
  def authMethod = column[String]("AUTH_METHOD")
  def oAuth1Id = column[Option[Long]]("OAUTH1_ID")
  def oAuth2Id = column[Option[Long]]("OAUTH2_ID")
  def passwordId = column[Option[Long]]("PASSWORD_ID")

  def * = (
    id.?,
    providerId,
    userId,
    firstName,
    lastName,
    fullName,
    email,
    avatarUrl,
    authMethod,
    oAuth1Id,
    oAuth2Id,
    passwordId
    ) <> (Profile.tupled, Profile.unapply)

  def idk = index("PROFILE_IDX", (providerId, userId))
}

object UserTableQueries {
  val mailTokens = TableQuery[MailTokens]
  val userAuthenticators = TableQuery[UserAuthenticators]
  val users = TableQuery[Users]
  val oauth1s = TableQuery[OAuth1s]
  val oauth2s = TableQuery[OAuth2s]
  val passwords = TableQuery[Passwords]
  val profiles = TableQuery[Profiles]
}


object UserDAO {
  val mailTokensQ = TableQuery[MailTokens]
  
  def insertMailToken(token: MailToken)(implicit s: Session) {
    Logger.debug("insertMailToken")
    mailTokensQ.insert(token)
    Logger.debug("insertMailToken - out")
  }
  
}