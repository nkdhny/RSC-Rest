package ru.rsc.ipmi.common

/**
 * User: alexey
 * Date: 8/23/13
 * Time: 10:36 PM
 */
trait CredentialsProvider {
  type Login = String
  type Password = String
  def getLogin(): Login
  def getPassword(): Password
}

trait StubCredentialsProvider extends CredentialsProvider {
  def getLogin(): StubCredentialsProvider#Login = "ADMIN"

  def getPassword(): StubCredentialsProvider#Password = "ADMIN"
}
