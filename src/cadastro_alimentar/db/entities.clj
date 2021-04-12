(ns cadastro-alimentar.db.entities
  (:use korma.core cadastro-alimentar.db)
  (:require [korma.core :refer [defentity pk belongs-to prepare transform entity-fields many-to-many has-many has-one]]))

(declare refeicoes tipos-alimento alimentos peso-alimentos)

(defentity refeicoes
  (pk :uuid)
  (table :refeicoes)
  (many-to-many alimentos :pesos_alimentos {:lfk :refeicao_uuid :rfk :alimento_uuid}))

(defentity tipos-alimentos
  (pk :uuid)
  (table :tipos_alimentos))

(defentity alimentos
  (pk :uuid)
  (table :alimentos)
  (belongs-to tipos-alimentos {:fk :tipo_alimento_uuid}))

(defentity pesos-alimentos
  (pk :uuid)
  (table :pesos_alimentos))