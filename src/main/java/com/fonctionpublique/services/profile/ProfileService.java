package com.fonctionpublique.services.profile;

import com.fonctionpublique.dto.ProfileDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ProfileService {


    ProfileDTO createProfile(ProfileDTO profileDTO);

    ProfileDTO updateProfile(int profileId, ProfileDTO profileDTO);
}
