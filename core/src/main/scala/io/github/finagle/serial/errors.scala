package io.github.finagle.serial

/**
 * Represents an error that occurs during encoding or decoding.
 */
case class CodecError(message: String) extends Exception(message)

/**
 * Represents an application error that an implementation does not know how to
 * encode or decode.
 *
 * While [[Serial]] implementations can provide a codec for any exceptions they
 * wish to pass through to clients, there is no requirement that this codec
 * should encode all possible errors. If the codec fails to encode an error, an
 * [[ApplicationError]] is returned that contains that error's message.
 */
case class ApplicationError(message: String) extends Exception(message)
