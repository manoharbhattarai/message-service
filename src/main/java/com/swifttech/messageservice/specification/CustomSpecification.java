package com.swifttech.messageservice.specification;

import com.swifttech.messageservice.model.Message;
import com.swifttech.messageservice.payload.request.MessageSearchFilterPaginationRequest;
import com.swifttech.messageservice.payload.request.NotificationDetailsRequest;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CustomSpecification {

    public static Specification<Message> filterMessage(MessageSearchFilterPaginationRequest  request){
        return (root, query, criteriaBuilder) -> {

            Predicate  finalPredicate = criteriaBuilder.conjunction(); //Initial empty predicate
            if (StringUtils.isNotBlank(request.getStatus())){
                Predicate statusPredicate = criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")),
                        request.getStatus()
                                .toLowerCase());
                finalPredicate = criteriaBuilder.and(finalPredicate, statusPredicate);
            }

            if (StringUtils.isNotBlank(request.getChannel())){
                Predicate channelPredicate = criteriaBuilder.equal(criteriaBuilder.lower(root
                                .get("channel")), request.getChannel()
                        .toLowerCase());
                finalPredicate = criteriaBuilder.and(finalPredicate,channelPredicate);
            }


            if (request.getCreatedDateFrom() != null && request.getCreatedDateTo() !=null){
                LocalDateTime from = LocalDateTime.parse(request.getCreatedDateFrom());
                LocalDateTime to = LocalDateTime.parse(request.getCreatedDateTo());
                Predicate createdDatePredicate = criteriaBuilder.between(root.get("createdAt"),
                        from, to);
                finalPredicate = criteriaBuilder.and(finalPredicate, createdDatePredicate);

            }
            if (StringUtils.isNotBlank(request.getSearchText())) {
                return criteriaBuilder.equal(
                        criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("customer"), criteriaBuilder.literal("nationality")),
                        request.getSearchText());
            }


            return finalPredicate;
        };

    }
    private static String likePattern(String value) {
        return "%" + value + "%";
    }

}
