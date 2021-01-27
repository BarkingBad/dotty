package dotty.dokka

import org.jetbrains.dokka.transformers.documentation.DocumentableTransformer
import org.jetbrains.dokka.model.{ Modifier => _, _ }
import collection.JavaConverters._
import org.jetbrains.dokka.plugability.DokkaContext
import org.jetbrains.dokka.model.properties._

import dotty.dokka.model._
import dotty.dokka.model.api._


class SealedMarksGraphTransformer(using context: DocContext) extends ModuleTransformer:
  override def apply(original: DModule): DModule =
    val sealedRelations = getSealedRelations(original.getPackages.get(0))
    original.updateMembers { m =>
      m.mapGraphEdges {
        case Edge(from @ LinkToType(_, fromDri, _), to @ LinkToType(_, toDri, _), _) =>
          val desc = if sealedRelations.contains(toDri) then "🔒" else ""
          Edge(from, to, desc)
      }
    }

  private def getSealedRelations(c: Member): Set[DRI] =
    val selfMapping = if c.modifiers.contains(Modifier.Sealed) then Set(c.dri) else Set.empty
    c.allMembers.flatMap(getSealedRelations).toSet ++ selfMapping
