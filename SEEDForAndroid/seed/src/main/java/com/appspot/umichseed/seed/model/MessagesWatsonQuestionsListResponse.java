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
 * on 2014-12-04 at 22:24:58 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a list of watson question/answer pair
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessagesWatsonQuestionsListResponse extends com.google.api.client.json.GenericJson {

  /**
   * ProtoRPC message definition to represent a watson question/answer pair
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<MessagesWatsonQuestionPut> questions;

  /**
   * ProtoRPC message definition to represent a watson question/answer pair
   * @return value or {@code null} for none
   */
  public java.util.List<MessagesWatsonQuestionPut> getQuestions() {
    return questions;
  }

  /**
   * ProtoRPC message definition to represent a watson question/answer pair
   * @param questions questions or {@code null} for none
   */
  public MessagesWatsonQuestionsListResponse setQuestions(java.util.List<MessagesWatsonQuestionPut> questions) {
    this.questions = questions;
    return this;
  }

  @Override
  public MessagesWatsonQuestionsListResponse set(String fieldName, Object value) {
    return (MessagesWatsonQuestionsListResponse) super.set(fieldName, value);
  }

  @Override
  public MessagesWatsonQuestionsListResponse clone() {
    return (MessagesWatsonQuestionsListResponse) super.clone();
  }

}
