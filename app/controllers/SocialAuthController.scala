package controllers

import akka.parboiled2.RuleTrace.Action
import com.mohiva.play.silhouette.api.LoginEvent
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.impl.providers._
import play.api.mvc.Results.Redirect

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Cookie, Request}
import play.filters.csrf.CSRF.Token
import play.filters.csrf.{CSRF, CSRFAddToken}
import repositories.UserRepository

import scala.concurrent.{ExecutionContext, Future}
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.impl.providers._
import controllers.DefaultSilhouetteControllerComponents
import models.User

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Cookie, Request}
import play.filters.csrf.CSRF.Token
import play.filters.csrf.{CSRF, CSRFAddToken}
import slick.jdbc.H2Profile.profile
import slick.lifted.Functions.user

import scala.concurrent.{ExecutionContext, Future}
/*
class SocialAuthController @Inject()(scc: DefaultSilhouetteControllerComponents, addToken: CSRFAddToken)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {

  def authenticate(provider: String): Action[AnyContent] = addToken(Action.async { implicit request: Request[AnyContent] =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            res <- userRepository.getByEmail(profile.email.getOrElse(""))
            user <- if (res == null) userRepository.create(profile.loginInfo.providerID, profile.loginInfo.providerKey, profile.email.getOrElse("")) else userRepository.getByEmail(profile.email.getOrElse(""))
            _ <- authInfoRepository.save(profile.loginInfo, authInfo)
            authenticator <- authenticatorService.create(profile.loginInfo)
            value <- authenticatorService.init(authenticator)
            result <- authenticatorService.embed(value, Redirect(s"http://localhost:3000?user-id=${user}"))
          } yield {
            val Token(name, value) = CSRF.getToken.get
            result.withCookies(Cookie(name, value, httpOnly = false))
          }
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case _: ProviderException =>
        Forbidden("Tutaj sypie forbiddenem")
    }
  })
}
 */

class SocialAuthController @Inject()(scc: DefaultSilhouetteControllerComponents, addToken: CSRFAddToken)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {

  def authenticate(provider: String): Action[AnyContent] = addToken(Action.async { implicit request: Request[AnyContent] =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            res <- userRepository.getByEmailOption(profile.email.getOrElse(""))
            user <- if (res.orNull == null) userRepository.create(profile.loginInfo.providerID, profile.loginInfo.providerKey, profile.email.getOrElse("")) else userRepository.getByEmail(profile.email.getOrElse(""))
            _ <- authInfoRepository.save(profile.loginInfo, authInfo)
            authenticator <- authenticatorService.create(profile.loginInfo)
            value <- authenticatorService.init(authenticator)
            result <- authenticatorService.embed(value, Redirect(s"http://localhost:3000?user-id=${user.id}"))
          } yield {
            val Token(name, value) = CSRF.getToken.get
            result.withCookies(Cookie(name, value, httpOnly = false))
          }
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    })/*.recover {
      case e: ProviderException =>
        print(e)
        Forbidden("Poleciał wyjątek")
    }*/
  })
 /* def authenticate(provider: String) = addToken(Action.async { implicit request =>
    ( (socialProviderRegistry.get[SocialProvider](provider)) match {
      case ( Some(p: SocialProvider with CommonSocialProfileBuilder) ) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) =>
        for {
          profile <- p.retrieveProfile(authInfo)
          user <- userRepository.create(profile.loginInfo.providerID,profile.loginInfo.providerKey, profile.email.getOrElse(""))
          authInfo <- authInfoRepository.save(profile.loginInfo, authInfo)
          authenticator <- silhouette.env.authenticatorService.create(profile.loginInfo)
          value <- silhouette.env.authenticatorService.init(authenticator)
          result <- authenticatorService.embed(value, Redirect(s"http://localhost:3000?user-id=${user.loginInfo.providerKey}"))
        } yield {
          silhouette.env.eventBus.publish(LoginEvent(user, request))
          result
        }

      case _ =>
        Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }}).recover {
      case e: ProviderException =>
        logger.error("Unexpected provider error", e)
        NotFound
    }
  })*/
}