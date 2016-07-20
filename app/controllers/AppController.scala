package controllers

import play.api.mvc._
import javax.inject._

@Singleton
class AppController extends Controller {

	def index = AuthenticatedAction(Ok(views.html.main.render()))

	def angular(any: Any) = index

}