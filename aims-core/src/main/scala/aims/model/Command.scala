package aims.model

/**
 * Component:
 * Description:
 * Date: 15/1/20
 * @author Andy Ai
 */
sealed trait Command

object Command {

  final case class INSERT() extends Command

  final case class UPDATE() extends Command

  final case class DELETE() extends Command

}

sealed trait Query

object Query {

  final case class GET() extends Query

  final case class QUERY() extends Query

}
