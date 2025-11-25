package com.algaworks.algashop.ordering.infrastructure.adapters.out.persistence.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, UUID> {
	Optional<ShoppingCartPersistenceEntity> findByCustomer_Id(UUID value);

	@Modifying
	@Transactional
	@Query("""
		UPDATE
			ShoppingCartItemPersistenceEntity i
		SET
			i.price = :price,
			i.totalAmount = :price * i.quantity
		WHERE
			i.productId = :productId
		""")
	void updateItemPrice(@Param("productId") UUID productId, @Param("price") BigDecimal price);

	@Modifying
	@Transactional
	@Query("""
		UPDATE
			ShoppingCartItemPersistenceEntity i
		SET
			i.available = :available
		WHERE
			i.productId = :productId
		""")
	void updateItemAvailability(@Param("productId") UUID productId, @Param("available") boolean available);

	@Modifying
	@Transactional
	@Query("""
		UPDATE
			ShoppingCartPersistenceEntity sc
		SET
			sc.totalAmount = (
				SELECT SUM(i.totalAmount)
				FROM ShoppingCartItemPersistenceEntity i
				WHERE i.shoppingCart.id = sc.id
			)
		WHERE
			EXISTS (SELECT 1
				FROM ShoppingCartItemPersistenceEntity i2
				WHERE i2.shoppingCart.id = sc.id
				AND i2.productId = :productId)
		""")
	void recalculateTotalsForCartsWithProduct(@Param("productId") UUID productId);
}