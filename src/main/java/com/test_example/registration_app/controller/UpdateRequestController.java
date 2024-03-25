package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.repository.RequestRepository;
import com.test_example.registration_app.service.RequestDtoConverterService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/update")
@RequiredArgsConstructor
public class UpdateRequestController {

    private final RequestRepository requestRepository;

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long idRequest, Model model) {
        Request request = requestRepository.findById(idRequest)
                .orElseThrow(() -> new IllegalArgumentException("(GetMapping)Invalid request Id: " + idRequest));

        if (!request.getStatus().equals(EnumStatus.DRAFT)){
            return "redirect:/api/created-request/" + request.getId();
        }

        RequestDto requestDto = RequestDtoConverterService.fromRequestToRequestDto(request);
        model.addAttribute("request", requestDto);
        model.addAttribute("requestId", idRequest);
        return "edit_request_by_id";
    }

    @PostMapping("/edit")
    public String updateRequest(@RequestParam Long idRequest, RequestDto requestDto) {
        Request requestToUpdate = requestRepository.findById(idRequest)
                .orElseThrow(() -> new IllegalArgumentException("(PostMapping)Invalid request Id: " + idRequest));

        if (!UpdateRequestService.isUpdated(requestToUpdate, requestDto)) {
            Request updatedRequest = UpdateRequestService.updateRequestFromDto(requestToUpdate, requestDto);
            requestRepository.saveAndFlush(updatedRequest);
            log.info("Request with ID: {} has been updated", idRequest);
        } else {
            log.info("No changes detected for request with ID: {}. Skipping database update.", idRequest);
        }
        return "redirect:/api/created-request/" + requestToUpdate.getId();
    }
}
