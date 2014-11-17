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
 * (build: 2014-10-28 17:08:27 UTC)
 * on 2014-11-10 at 20:29:53 UTC 
 * Modify at your own risk.
 */

package com.appspot.umichseed.seed.model;

/**
 * ProtoRPC message definition to represent a patient data query
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the seed. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class SeedApiMessagesPQuantDataRequest extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String email;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("end_time")
  private com.google.api.client.util.DateTime endTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("start_time")
  private com.google.api.client.util.DateTime startTime;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getEmail() {
    return email;
  }

  /**
   * @param email email or {@code null} for none
   */
  public SeedApiMessagesPQuantDataRequest setEmail(java.lang.String email) {
    this.email = email;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getEndTime() {
    return endTime;
  }

  /**
   * @param endTime endTime or {@code null} for none
   */
  public SeedApiMessagesPQuantDataRequest setEndTime(com.google.api.client.util.DateTime endTime) {
    this.endTime = endTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getStartTime() {
    return startTime;
  }

  /**
   * @param startTime startTime or {@code null} for none
   */
  public SeedApiMessagesPQuantDataRequest setStartTime(com.google.api.client.util.DateTime startTime) {
    this.startTime = startTime;
    return this;
  }

  @Override
  public SeedApiMessagesPQuantDataRequest set(String fieldName, Object value) {
    return (SeedApiMessagesPQuantDataRequest) super.set(fieldName, value);
  }

  @Override
  public SeedApiMessagesPQuantDataRequest clone() {
    return (SeedApiMessagesPQuantDataRequest) super.clone();
  }

}
