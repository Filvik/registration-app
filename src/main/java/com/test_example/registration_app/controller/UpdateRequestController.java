package com.test_example.registration_app.controller;

import com.test_example.registration_app.dtos.RequestDto;
import com.test_example.registration_app.enums.EnumStatus;
import com.test_example.registration_app.model.Request;
import com.test_example.registration_app.service.RequestDtoConverterService;
import com.test_example.registration_app.service.UpdateRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/update")
@RequiredArgsConstructor
public class UpdateRequestController {

    private final UpdateRequestService updateRequestService;
    private final RequestDtoConverterService requestDtoConverterService;

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long idRequest, Model model, RedirectAttributes redirectAttributes) {

        try {
            Request request = updateRequestService.getRequestById(idRequest);
            if (!request.getStatus().equals(EnumStatus.DRAFT)){
                return "redirect:/api/created-request/" + request.getId();
            }
            RequestDto requestDto = requestDtoConverterService.fromRequestToRequestDto(request);
            model.addAttribute("request", requestDto);
            model.addAttribute("idRequest", idRequest);
            return "edit_request_by_id";
        }
        catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }

    @PutMapping("/edit")
    public String updateRequest(@RequestParam Long idRequest, RequestDto requestDto, RedirectAttributes redirectAttributes) {
        try {
            Request requestToUpdate = updateRequestService.getRequestById(idRequest);
            if (!UpdateRequestService.isUpdated(requestToUpdate, requestDto)) {
                updateRequestService.updateRequestFromDto(requestToUpdate, requestDto);
                log.info("Request with ID: {} has been updated", idRequest);
            } else {
                log.info("No changes detected for request with ID: {}. Skipping database update.", idRequest);
            }
            return "redirect:/api/created-request/" + requestToUpdate.getId();
        }
        catch (Exception e) {
            log.warn("Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/error";
        }
    }
}
