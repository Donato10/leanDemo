package org.lean.persistence.specification;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.lean.model.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@EqualsAndHashCode
public class EmployeeSpecification implements Specification<Employee> {
    private final EmployeeSearchCriteria searchCriteria;
    private final String sortBy;

    private Predicate getPredicate(CriteriaBuilder builder, Root<Employee> root, String path, Object matchValue) {
        return builder.equal(processPath(root, path), matchValue);
    }

    @Override
    public Predicate toPredicate(
        Root<Employee> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
    {
        List<Predicate> criterias = new ArrayList<>();
        searchCriteria.getPathValues().forEach((String path, Object value) ->
                                                   criterias.add(getPredicate(criteriaBuilder, root, path, value))
        );
        criteriaQuery.orderBy(criteriaBuilder.desc(processPath(root, sortBy)));
        return criteriaBuilder.and(criterias.toArray(new Predicate[0]));
    }

    private Expression<String> processPath(Root<Employee> root, String path) {
        String[] parts = path.split(EmployeeSearchCriteria.PATH_SEPARATOR);
        Assert.state(parts.length > 0, "Unexpected criteria field path: " + path);
        Path<String> rootName = root.get(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            rootName = rootName.get(parts[i]);
        }
        return rootName;
    }
}
