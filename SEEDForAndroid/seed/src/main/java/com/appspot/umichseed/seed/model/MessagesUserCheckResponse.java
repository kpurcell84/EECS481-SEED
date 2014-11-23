/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-11-17 18:43:33 UTC)
 * on 2014-11-23 at 20:28:21 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a response about the type of user type = (Patient |
 * Doctor | None)
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessagesUserCheckResponse extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("user_type")
  private java.lang.String userType;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUserType() {
    return userType;
  }

  /**
   * @param userType userType or {@code null} for none
   */
  public MessagesUserCheckResponse setUserType(java.lang.String userType) {
    this.userType = userType;
    return this;
  }

  @Override
  public MessagesUserCheckResponse set(String fieldName, Object value) {
    return (MessagesUserCheckResponse) super.set(fieldName, value);
  }

  @Override
  public MessagesUserCheckResponse clone() {
    return (MessagesUserCheckResponse) super.clone();
  }

}
