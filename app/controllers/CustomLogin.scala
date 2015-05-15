package controllers

import securesocial.controllers.BaseLoginPage
import play.api.mvc.{ RequestHeader, AnyContent, Action }
import play.api.Logger
import securesocial.core.{ RuntimeEnvironment, IdentityProvider }
import securesocial.core.services.RoutesService
import models.BasicUser

/**
 * Not used at the moment, just left here for reference
 */
class CustomLogin(implicit override val env: RuntimeEnvironment[BasicUser]) extends BaseLoginPage[BasicUser] {
  override def login: Action[AnyContent] = {
    Logger.debug("using CustomLogin")
    super.login
  }
}

class CustomRoutesService extends RoutesService.Default {
//  override def loginPageUrl(implicit req: RequestHeader): String = controllers.routes.CustomLogin.login().absoluteURL(IdentityProvider.sslEnabled)
}