package com.jeannypr.scientificstudy.Timetable.api;

import com.jeannypr.scientificstudy.Teacher.model.TeacherProfileBean;
import com.jeannypr.scientificstudy.Timetable.model.SchoolShiftsBean;
import com.jeannypr.scientificstudy.Timetable.model.TimetableBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TimetableService {

    @GET("school/shifts")
    Call<SchoolShiftsBean> GetSchoolShifts(@Query("schoolid") int schoolId);

    @GET("class/timetable")
    Call<TimetableBean> GetClassTimetable(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId,
                                          @Query("classId") int ClassId, @Query("shiftId") int ShiftId,
                                          @Query("year") int Year, @Query("month") int Month, @Query("day") int Day);

    @GET("teacher/timetable")
    Call<TimetableBean> GetTeacherTimetable(@Query("schoolid") int SchoolId, @Query("academicYearid") int AcademicYearId,
                                          @Query("teacherUserId") int TeacherId, @Query("shiftId") int ShiftId,
                                          @Query("year") int Year, @Query("month") int Month, @Query("day") int Day);
}
