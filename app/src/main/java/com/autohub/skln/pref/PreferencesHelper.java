package com.autohub.skln.pref;


public interface PreferencesHelper {

    String getUserFirstName();

    String getUserLastName();

    String getUserPhone();

    String getUserPassword();

    String getUserId();

    String getOccupation();

    String getExperince();

    String getQualification();

    String getArea();

    String getUniversity();

    String getSpecialisation();

    String getYear();

    String getboard();

    String getexperinceLevel();

    String getworkingHour();

    String getOverview();

    String getClassesPlace();

    String getClasses();

    String getSubjects();

    String getProfileImage();

    String getCertificatesImage();

    String getProofImage();

    void setTutorMainInfo(String firstName, String lastName, String password);

    void setTutorId(String id);

    void setTutorPhoneNumber(String phone);

    void setUserScreenSix(String occupation, String experince, String qualification, String area, String university, String specialisation, String year, String profileImage, String certificateImage, String proofImage);


    void setUserScreenSeven(String board, String experinceLevel, String workingHour);

//    void setUserScreenSevenForHobbies(String experinceLevel, String workingHour);

    void setTutorCategory(String category);

    void setTutorBiodata(String overview);

    void setScreenNine(String classesPlace);

    void setScreenFour(String classes);

    void setTutorSubject(String subjects);
}