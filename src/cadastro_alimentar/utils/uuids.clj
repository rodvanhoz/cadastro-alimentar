(ns cadastro-alimentar.utils.uuids
  (:import (java.util Properties)))

(defn uuid
  []
  (java.util.UUID/randomUUID))

(defn uuid-from-string
  [uuid]
  (when uuid
    (if (uuid? uuid)
      uuid
      (java.util.UUID/fromString uuid))))