(ns cadastro-alimentar.db.entities
  (:use korma.core cadastro-alimentar.db)
  (:require [korma.core :refer [defentity pk belongs-to prepare transform entity-fields many-to-many has-many has-one]]))

(defentity refeicoes
  (pk :id_refeicao)
  (table :refeicoes))

(defentity tipos-alimento
  (pk :id_tipo_alimento)
  (table :tipos_alimento))

(defentity alimentos
  (pk :id_alimento)
  (table :alimentos)
  (belongs-to tipos-alimento {:fk :id_tipo_alimento}))

(defentity peso-alimento
  (pk :id_peso_alimento)
  (table :peso_alimentos)
  (belongs-to alimentos {:fk :id_alimento})
  (belongs-to refeicoes {:fk :id_refeicao}))