package com.htec.filesystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.servlet.view.RedirectView;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadImageResponseModel {

    RedirectView redirectView;
}
