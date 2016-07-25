package controllers

import play.api.mvc._
import javax.inject._
import play.api.Logger

@Singleton
class AppController extends Controller {

	def index = AuthenticatedAction( implicit request => {
	  Ok(views.html.main.render())
	})

	def angular(any: Any) = index

}