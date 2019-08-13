package com.jslsolucoes.nginx.admin.repository.impl;

import com.jslsolucoes.i18n.Messages;
import com.jslsolucoes.nginx.admin.model.*;
import com.jslsolucoes.nginx.admin.repository.ResourceIdentifierRepository;
import com.jslsolucoes.nginx.admin.repository.VirtualHostAliasRepository;
import com.jslsolucoes.nginx.admin.repository.VirtualHostLocationRepository;
import com.jslsolucoes.nginx.admin.repository.VirtualHostRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class VirtualHostRepositoryImpl extends RepositoryImpl<VirtualHost> implements VirtualHostRepository {

	private ResourceIdentifierRepository  resourceIdentifierRepository;
	private VirtualHostAliasRepository    virtualHostAliasRepository;
	private VirtualHostLocationRepository virtualHostLocationRepository;

	@Deprecated
	public VirtualHostRepositoryImpl() {

	}

	@Inject
	public VirtualHostRepositoryImpl(EntityManager entityManager,
									 ResourceIdentifierRepository resourceIdentifierRepository,
									 VirtualHostAliasRepository virtualHostAliasRepository,
									 VirtualHostLocationRepository virtualHostLocationRepository) {
		super(entityManager);
		this.resourceIdentifierRepository = resourceIdentifierRepository;
		this.virtualHostAliasRepository = virtualHostAliasRepository;
		this.virtualHostLocationRepository = virtualHostLocationRepository;
	}

	@Override
	public OperationResult saveOrUpdate(VirtualHost virtualHost, List<VirtualHostAlias> aliases,
										List<VirtualHostLocation> locations) {
		if (virtualHost.getHttps() == 0) {
			virtualHost.setSslCertificate(null);
		}
		OperationResult operationResult = super.saveOrUpdate(virtualHost);
		virtualHostAliasRepository.recreateAllFor(new VirtualHost(operationResult.getId()), aliases);
		virtualHostLocationRepository.recreateAllFor(new VirtualHost(operationResult.getId()), locations);
		return operationResult;
	}

	@Override
	public OperationStatusType delete(VirtualHost virtualHost) {
		virtualHostAliasRepository.deleteAllFor(virtualHost);
		virtualHostLocationRepository.deleteAllFor(virtualHost);
		VirtualHost virtualHostToDelete = load(virtualHost);
		String uuid = virtualHostToDelete.getResourceIdentifier().getUuid();
		super.delete(virtualHostToDelete);
		resourceIdentifierRepository.delete(uuid);
		return OperationStatusType.DELETE;
	}

	@Override
	public List<String> validateBeforeSaveOrUpdate(VirtualHost virtualHost, List<VirtualHostAlias> aliases,
												   List<VirtualHostLocation> locations) {
		List<String> errors = new ArrayList<>();

		if (hasEquals(virtualHost, aliases) != null) {
			errors.add(Messages.getString("virtualHost.already.exists"));
		}

		if (aliases.stream().map(VirtualHostAlias::getAlias).collect(Collectors.toSet()).size() != aliases.size()) {
			errors.add(Messages.getString("virtualHost.alias.mapped.twice"));
		}

		if (locations.stream().map(VirtualHostLocation::getPath).collect(Collectors.toSet()).size() != locations
				.size()) {
			errors.add(Messages.getString("virtualHost.location.mapped.twice"));
		}

		return errors;
	}

	@Override
	public VirtualHost hasEquals(VirtualHost virtualHost, List<VirtualHostAlias> aliases) {
		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<VirtualHost> criteriaQuery = criteriaBuilder.createQuery(VirtualHost.class);
			Root<VirtualHost> root = criteriaQuery.from(VirtualHost.class);
			SetJoin<VirtualHost, VirtualHostAlias> join = root.join(VirtualHost_.aliases, JoinType.INNER);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.equal(root.join(VirtualHost_.nginx, JoinType.INNER).get(Nginx_.id),
					virtualHost.getNginx().getId()));
			predicates.add(join.get(VirtualHostAlias_.alias)
					.in(aliases.stream().map(VirtualHostAlias::getAlias).collect(Collectors.toList())));
			if (virtualHost.getId() != null) {
				predicates.add(criteriaBuilder.notEqual(root.get(VirtualHost_.id), virtualHost.getId()));
			}
			criteriaQuery.distinct(true).where(predicates.toArray(new Predicate[] {}));
			return entityManager.createQuery(criteriaQuery).getSingleResult();
		} catch (NoResultException noResultException) {
			return null;
		}
	}

	@Override
	public List<VirtualHost> listAllFor(Nginx nginx) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VirtualHost> criteriaQuery = criteriaBuilder.createQuery(VirtualHost.class);
		Root<VirtualHost> root = criteriaQuery.from(VirtualHost.class);
		criteriaQuery.where(
				criteriaBuilder.equal(root.join(VirtualHost_.nginx, JoinType.INNER).get(Nginx_.id), nginx.getId()));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public List<VirtualHost> searchFor(Nginx nginx, String term) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<VirtualHost> criteriaQuery = criteriaBuilder.createQuery(VirtualHost.class);
		Root<VirtualHost> root = criteriaQuery.from(VirtualHost.class);
		SetJoin<VirtualHost, VirtualHostAlias> join = root.join(VirtualHost_.aliases, JoinType.INNER);
		criteriaQuery.where(criteriaBuilder.and(
				criteriaBuilder.equal(root.join(VirtualHost_.nginx, JoinType.INNER).get(Nginx_.id), nginx.getId()),
				criteriaBuilder.like(criteriaBuilder.lower(join.get(VirtualHostAlias_.alias)), term.toLowerCase())));
		return entityManager.createQuery(criteriaQuery).getResultList();
	}
}
