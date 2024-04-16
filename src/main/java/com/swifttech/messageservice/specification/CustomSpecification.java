package com.swifttech.messageservice.specification;

import com.swifttech.messageservice.entity.MessageEntity;
import com.swifttech.messageservice.payload.request.MessageSearchFilterPaginationRequest;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CustomSpecification {

    public static Specification<MessageEntity> filterMessage(MessageSearchFilterPaginationRequest request) {
        return (root, query, criteriaBuilder) -> {

            Predicate finalPredicate = criteriaBuilder.conjunction(); //Initial empty predicate
            if (StringUtils.isNotBlank(request.getStatus())) {
                Predicate statusPredicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
                        request.getStatus()
                                .toLowerCase());
                finalPredicate = criteriaBuilder.and(finalPredicate, statusPredicate);
            }

            if (StringUtils.isNotBlank(request.getChannelMode())) {
                Predicate channelPredicate = criteriaBuilder.equal(criteriaBuilder.lower(root
                        .get("channel")), request.getChannelMode()
                        .toLowerCase());
                finalPredicate = criteriaBuilder.and(finalPredicate, channelPredicate);
            }


            if (request.getCreatedDateFrom() != null && request.getCreatedDateTo() != null) {
                LocalDateTime from = LocalDateTime.parse(request.getCreatedDateFrom());
                LocalDateTime to = LocalDateTime.parse(request.getCreatedDateTo());
                Predicate createdDatePredicate = criteriaBuilder.between(root.get("createdAt"),
                        from, to);
                finalPredicate = criteriaBuilder.and(finalPredicate, createdDatePredicate);

            }
            if (StringUtils.isNotBlank(request.getSearchText())) {
                Predicate predicate =
                        //criteriaBuilder.or
//                        (criteriaBuilder.like(root.get("channelMode"),likePattern(request.getSearchText())),
                        criteriaBuilder.like(
                        criteriaBuilder.function("LOWER", String.class,
                                criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("customer"),
                                        criteriaBuilder.literal("nationality"))),
                        likePattern(request.getSearchText().toLowerCase()));
                finalPredicate = criteriaBuilder.and(finalPredicate, predicate);
            }


            return finalPredicate;
        };

    }

    private static String likePattern(String value) {
        return "%" + value + "%";
    }

}
