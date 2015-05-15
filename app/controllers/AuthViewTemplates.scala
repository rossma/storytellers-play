package controllers

import play.api.data.Form
import play.api.i18n.Lang
import play.api.mvc.RequestHeader
import play.twirl.api.{ Html, Txt }
import securesocial.core.{ BasicProfile, RuntimeEnvironment }
import securesocial.controllers.{ChangeInfo, RegistrationInfo, ViewTemplates}

/**
 * Authorisation View Template registered in the applications RuntimeEnvironment and 
 * used to override the default socialsecure view template in order to customise the views.
 */
class AuthViewTemplates(env: RuntimeEnvironment[_]) extends ViewTemplates {
    implicit val implicitEnv = env
    
    override def getLoginPage(form: Form[(String, String)],
      msg: Option[String] = None)(implicit request: RequestHeader, lang: Lang): Html = {
      //securesocial.views.html.login(form, msg)
      views.html.login(form, msg)
    }

    override def getSignUpPage(form: Form[RegistrationInfo], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
      securesocial.views.html.Registration.signUp(form, token)
    }

    override def getStartSignUpPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
      securesocial.views.html.Registration.startSignUp(form)
    }

    override def getStartResetPasswordPage(form: Form[String])(implicit request: RequestHeader, lang: Lang): Html = {
      securesocial.views.html.Registration.startResetPassword(form)
    }

    override def getResetPasswordPage(form: Form[(String, String)], token: String)(implicit request: RequestHeader, lang: Lang): Html = {
      securesocial.views.html.Registration.resetPasswordPage(form, token)
    }

    override def getPasswordChangePage(form: Form[ChangeInfo])(implicit request: RequestHeader, lang: Lang): Html = {
      securesocial.views.html.passwordChange(form)
    }

    def getNotAuthorizedPage(implicit request: RequestHeader, lang: Lang): Html = {
      securesocial.views.html.notAuthorized()
    }
}