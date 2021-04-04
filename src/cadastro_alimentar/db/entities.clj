(ns cadastro-alimentar.db.entities
  (:use korma.core cadastro-alimentar.db)
  (:require [korma.core :refer [defentity pk belongs-to prepare transform entity-fields many-to-many has-many has-one]]))

(declare refeicoes tipos-alimento alimentos peso-alimentos)

(defentity refeicoes
  (pk :id_refeicao)
  (table :refeicoes)
  (many-to-many alimentos :peso_alimento {:lfk :id_refeicao :rfk :id_alimento}))

(defentity tipos-alimento
  (pk :id_tipo_alimento)
  (table :tipos_alimento))

(defentity alimentos
  (pk :id_alimento)
  (table :alimentos)
  (belongs-to tipos-alimento {:fk :id_tipo_alimento}))

(defentity peso-alimentos
  (pk :id_peso_alimento)
  (table :peso_alimento))