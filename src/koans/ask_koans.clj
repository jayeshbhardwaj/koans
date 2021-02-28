(ns koans.ask-koans
  (:gen-class
   :name koans.askLambda
   :methods [[handleLambda [java.util.Map] java.util.Map]])
  (:use [koans.koan-dao]))


(defn create-simple-response
  "Creates a skill response with the given title and text. Optionally closes session.
  The response specifies output speech and card."
  [text shouldEndSession]
  {"version"           "1.0"
   "sessionAttributes" {}
   "response"          {"outputSpeech"     {"type" "PlainText"
                                            "text" text}

                        "shouldEndSession" shouldEndSession}})


(def help-text "Say tell me a zen koan to get started.")

(defn extract-intent-name
  "Extracts intent name from the given skill request structure."
  [request & not-found]
  (get-in request ["request" "intent" "name"] not-found))

(defmulti handle-intent
  (fn [req] (-> req extract-intent-name keyword)))

(defmethod handle-intent :AMAZON.StopIntent [request]
  ["Okay, Stopping." true])

(defmethod handle-intent :AMAZON.CancelIntent [request]
  ["Okay, Cancelling." true])


(defmethod handle-intent :AMAZON.HelpIntent [_]
  [help-text false])

(defmethod handle-intent :default [_]
  [help-text false])


(defmethod handle-intent :AskKoanIntent [request]
  [(get zen-koans (inc (rand-int total))) false])


(defn -handleLambda
  "Entry point for Amazon Lambda call triggered by Alexa.
  Alexa passes in its input as json (transparently parsed to a Map).
  The expected output is Alexa response in the form of a Map (which too will be converted to json by Jackson under the hood)."
  [obj input]
  (apply create-simple-response  (handle-intent input)))
