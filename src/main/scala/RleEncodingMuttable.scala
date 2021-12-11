import scala.collection.mutable.Queue

object RleEncodingMuttableApp extends App {

  val rleEncoding = new RleEncodingMuttable

  //  println(rleEncoding.encode("ABCDE")) // A1B1C1D1E1
  //
  //  println(rleEncoding.encode("AABBCCDD")) // A2B2C2D2
//    println(rleEncoding.encode("XAABBCCDD")) // FIXME: 2XA2AB2BC2CD
  //
  //  println(rleEncoding.encode("AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")) // A4B3C2XYZD4E3F3A6B29
  println(rleEncoding.encode("AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBX" * 10000)) // FIXME: last symbol is lost
}

trait BlockM {
  def length: Int
}

case class UncompressedBlockM(length: Int, data: Seq[Char]) extends BlockM

case class CompressedBlockM(length: Int, data: Char) extends BlockM

case object EmptyBlockM extends BlockM {
  override val length: Int = 0
}


class RleEncodingMuttable {

  def encode(str: String): Queue[BlockM] = {
    val (prev, optBlock, result) =
      str.toCharArray.foldLeft((None: Option[Char], EmptyBlockM:BlockM, Queue.empty[BlockM])) {
        case ((None, _, result), char) =>
          (Some(char), EmptyBlockM, result)

        case ((Some(prev), EmptyBlockM, result), char) if prev == char =>
          (Some(char), CompressedBlockM(2, prev), result)

        case ((Some(prev), EmptyBlockM, result), char) =>
          (Some(char), UncompressedBlockM(1, Seq(prev)), result)

        case ((Some(prev), block@CompressedBlockM(_, _), result), char) if prev == char =>
          (Some(char), CompressedBlockM(block.length + 1, block.data), result)

        case ((Some(_), block@CompressedBlockM(_, _), result), char) =>
          (Some(char), EmptyBlockM, result :+ block)

        case ((Some(prev), block@UncompressedBlockM(_, _), result), char) if prev != char =>
          (Some(char), UncompressedBlockM(block.length + 1, block.data :+ prev), result)

        case ((Some(_), block@UncompressedBlockM(_, _), result), char) =>
          (Some(char), CompressedBlockM(2, char), result :+ block)

      }

    optBlock match {
      case EmptyBlockM => result :+ UncompressedBlockM(1, prev.toList)
      case UncompressedBlockM(length, seq) => result :+ UncompressedBlockM(length + 1, seq ++ prev.toList)
      case block => result :+ block
    }

  }
}
