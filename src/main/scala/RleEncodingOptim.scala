import scala.annotation.tailrec


object RleEncodingOptimApp extends App {
  val rleEncoding = new RleEncodingOptim

//    println(rleEncoding.encode("ABCDE")) // A1B1C1D1E1
//    println(rleEncoding.encode("AABBCCDD")) // A2B2C2D2
//    println(rleEncoding.encode("XAABBCCDD")) // FIXME: 2XA2AB2BC2CD
//
//    println(rleEncoding.encode("AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBB")) // A4B3C2XYZD4E3F3A6B29

    println(rleEncoding.encode("AAAABBBCCXYZDDDDEEEFFFAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBX" * 100000)) // FIXME: last symbol is lost
}

//trait Block {
//  def length: Int
//}
case class UncompressedBlockO(length: Int, list: List[Char]) extends Block
case class CompressedBlockO(length: Int, data: Char) extends Block

sealed trait TypeBlock

case object Unknown extends TypeBlock
case object Uncompressed extends TypeBlock
case object Compressed extends TypeBlock

class RleEncodingOptim {
  def encode(str: String): List[Block] = {
    val listChar = str.toList

    @tailrec
    def go(prev:Char, listChar:List[Char], list:List[Char] = Nil, typePrevBl:TypeBlock = Unknown, length: Int = 0, res: List[Block] = Nil):List[Block] =
      listChar match {
        case Nil if (typePrevBl == Compressed) =>
          CompressedBlockO(length + 1, list.head) +: res
        case Nil =>
          UncompressedBlockO(length + 1, ( prev +: list ).reverse ) +: res
        case head :: _ if ((prev == head) && (typePrevBl == Uncompressed) ) =>
          go(head, listChar.tail, prev +: list, Compressed, 1, UncompressedBlockO(length, list.reverse ) +: res)
        case head :: _ if (prev == head) =>
          go(head, listChar.tail, prev +: list ,Compressed, length + 1, res)
        case head :: _ if ((prev != head) && (typePrevBl == Compressed) ) =>
          go(head, listChar.tail, Nil, Unknown, 0, CompressedBlockO(length + 1, list.head) +: res)
        case head :: _ if (prev != head) =>
          go(head, listChar.tail, prev +: list ,Uncompressed, length + 1, res)
      }
    if(listChar.isEmpty) Nil
    else
    go(listChar.head, listChar.tail).reverse
  }
}
