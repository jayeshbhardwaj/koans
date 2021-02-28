(ns koans.utils)

(def help-text "Say tell me a zen koan to get started.")

(defn extract-intent-name
  "Extracts intent name from the given skill request structure."
  [request & not-found]
  (get-in request ["request" "intent" "name"] not-found))

(defn create-simple-response
  "Creates a skill response with the given title and text. Optionally closes session.
  The response specifies output speech and card."
  [text shouldEndSession]
  {"version"           "1.0"
   "sessionAttributes" {}
   "response"          {"outputSpeech"     {"type" "PlainText"
                                            "text" text}

                        "shouldEndSession" shouldEndSession}})
