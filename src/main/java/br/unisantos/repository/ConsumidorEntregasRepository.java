package br.unisantos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.unisantos.model.ConsumidorEntregas;

@Repository
public interface ConsumidorEntregasRepository extends JpaRepository<ConsumidorEntregas, Long> {

}
