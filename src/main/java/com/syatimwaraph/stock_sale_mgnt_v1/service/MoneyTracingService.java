package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.MoneyTracingDecisionRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.MoneyTracingRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.MoneyTracingResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;

import com.syatimwaraph.stock_sale_mgnt_v1.entity.MoneyTracing;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyDecision;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.MoneyTracingStatus;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.AppUserRepository;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.MoneyTracingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MoneyTracingService {

    private final MoneyTracingRepository moneyTracingRepository;
    private final AppUserRepository userRepository;


    // ============================================================
    // CREATE
    // MANAGER
    // ============================================================

    public MoneyTracingResponse create(
            MoneyTracingRequest request,
            String email) {

        AppUser manager = getUserByEmail(email);

        MoneyTracing moneyTracing = MoneyTracing.builder()
                .amount(request.getAmount())
                .purpose(request.getPurpose().trim())
                .reference(normalize(request.getReference()))
                .notes(normalize(request.getNotes()))
                .submittedBy(manager)
                .submittedAt(LocalDateTime.now())
                .status(MoneyTracingStatus.PENDING_CASHIER)
                .build();

        MoneyTracing saved =
                moneyTracingRepository.save(moneyTracing);

        return mapToResponse(saved);
    }


    // ============================================================
    // GET ALL
    // ============================================================

    @Transactional(readOnly = true)
    public List<MoneyTracingResponse> getAll() {

        return moneyTracingRepository
                .findAllByOrderBySubmittedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET PENDING CASHIER
    // ============================================================

    @Transactional(readOnly = true)
    public List<MoneyTracingResponse> getPendingCashier() {

        return moneyTracingRepository
                .findByStatusOrderBySubmittedAtDesc(
                        MoneyTracingStatus.PENDING_CASHIER
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET PENDING ADMIN
    // ============================================================

    @Transactional(readOnly = true)
    public List<MoneyTracingResponse> getPendingAdmin() {

        return moneyTracingRepository
                .findByStatusOrderBySubmittedAtDesc(
                        MoneyTracingStatus.PENDING_ADMIN
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET ONE
    // ============================================================

    @Transactional(readOnly = true)
    public MoneyTracingResponse getById(Long id) {

        MoneyTracing moneyTracing =
                moneyTracingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Money tracing record not found: " + id
                                ));

        return mapToResponse(moneyTracing);
    }


    // ============================================================
    // CASHIER DECISION
    // ============================================================

    public MoneyTracingResponse cashierDecision(
            Long id,
            MoneyTracingDecisionRequest request,
            String email) {

        MoneyTracing moneyTracing =
                moneyTracingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Money tracing record not found: " + id
                                ));


        // --------------------------------------------------------
        // Must currently be waiting for cashier
        // --------------------------------------------------------

        if (moneyTracing.getStatus()
                != MoneyTracingStatus.PENDING_CASHIER) {

            throw new IllegalStateException(
                    "This money tracing record is not pending cashier verification."
            );
        }


        AppUser cashier = getUserByEmail(email);


        moneyTracing.setCashier(cashier);

        moneyTracing.setCashierDecision(
                request.getDecision()
        );

        moneyTracing.setCashierComment(
                normalize(request.getComment())
        );

        moneyTracing.setCashierProcessedAt(
                LocalDateTime.now()
        );


        // --------------------------------------------------------
        // CASHIER APPROVED
        // --------------------------------------------------------

        if (request.getDecision()
                == MoneyDecision.APPROVED) {

            moneyTracing.setStatus(
                    MoneyTracingStatus.PENDING_ADMIN
            );

        }

        // --------------------------------------------------------
        // CASHIER REJECTED
        // --------------------------------------------------------

        else {

            moneyTracing.setStatus(
                    MoneyTracingStatus.REJECTED
            );
        }


        MoneyTracing saved =
                moneyTracingRepository.save(moneyTracing);

        return mapToResponse(saved);
    }


    // ============================================================
    // ADMIN DECISION
    // ============================================================

    public MoneyTracingResponse adminDecision(
            Long id,
            MoneyTracingDecisionRequest request,
            String email) {

        MoneyTracing moneyTracing =
                moneyTracingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Money tracing record not found: " + id
                                ));


        // --------------------------------------------------------
        // Must currently be waiting for admin
        // --------------------------------------------------------

        if (moneyTracing.getStatus()
                != MoneyTracingStatus.PENDING_ADMIN) {

            throw new IllegalStateException(
                    "This money tracing record is not pending admin approval."
            );
        }


        AppUser admin = getUserByEmail(email);


        moneyTracing.setAdmin(admin);

        moneyTracing.setAdminDecision(
                request.getDecision()
        );

        moneyTracing.setAdminComment(
                normalize(request.getComment())
        );

        moneyTracing.setAdminProcessedAt(
                LocalDateTime.now()
        );


        // --------------------------------------------------------
        // ADMIN APPROVED
        // --------------------------------------------------------

        if (request.getDecision()
                == MoneyDecision.APPROVED) {

            moneyTracing.setStatus(
                    MoneyTracingStatus.COMPLETED
            );

        }

        // --------------------------------------------------------
        // ADMIN REJECTED
        // --------------------------------------------------------

        else {

            moneyTracing.setStatus(
                    MoneyTracingStatus.REJECTED
            );
        }


        MoneyTracing saved =
                moneyTracingRepository.save(moneyTracing);

        return mapToResponse(saved);
    }


    // ============================================================
    // GET AUTHENTICATED USER
    // ============================================================

    private AppUser getUserByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found."
                        ));
    }


    // ============================================================
    // NORMALIZE
    // ============================================================

    private String normalize(String value) {

        if (value == null ||
                value.trim().isEmpty()) {

            return null;
        }

        return value.trim();
    }


    // ============================================================
    // MAP ENTITY → RESPONSE
    // ============================================================

    private MoneyTracingResponse mapToResponse(
            MoneyTracing moneyTracing) {

        AppUser submittedBy =
                moneyTracing.getSubmittedBy();

        AppUser cashier =
                moneyTracing.getCashier();

        AppUser admin =
                moneyTracing.getAdmin();


        return MoneyTracingResponse.builder()

                .id(moneyTracing.getId())

                .amount(moneyTracing.getAmount())

                .purpose(moneyTracing.getPurpose())

                .reference(moneyTracing.getReference())

                .notes(moneyTracing.getNotes())


                // Manager

                .submittedById(
                        submittedBy != null
                                ? submittedBy.getId()
                                : null
                )

                .submittedByName(
                        getUserDisplayName(submittedBy)
                )

                .submittedAt(
                        moneyTracing.getSubmittedAt()
                )


                // Cashier

                .cashierId(
                        cashier != null
                                ? cashier.getId()
                                : null
                )

                .cashierName(
                        getUserDisplayName(cashier)
                )

                .cashierDecision(
                        moneyTracing.getCashierDecision()
                )

                .cashierComment(
                        moneyTracing.getCashierComment()
                )

                .cashierProcessedAt(
                        moneyTracing.getCashierProcessedAt()
                )


                // Admin

                .adminId(
                        admin != null
                                ? admin.getId()
                                : null
                )

                .adminName(
                        getUserDisplayName(admin)
                )

                .adminDecision(
                        moneyTracing.getAdminDecision()
                )

                .adminComment(
                        moneyTracing.getAdminComment()
                )

                .adminProcessedAt(
                        moneyTracing.getAdminProcessedAt()
                )


                // Status

                .status(
                        moneyTracing.getStatus()
                )

                .build();
    }


    // ============================================================
    // USER DISPLAY NAME
    // ============================================================

    private String getUserDisplayName(AppUser user) {

        if (user == null) {
            return null;
        }

        String firstName =
                user.getFirstName() != null
                        ? user.getFirstName()
                        : "";

        String lastName =
                user.getLastName() != null
                        ? user.getLastName()
                        : "";

        String fullName =
                (firstName + " " + lastName).trim();

        if (!fullName.isEmpty()) {
            return fullName;
        }

        return user.getEmail();
    }
}