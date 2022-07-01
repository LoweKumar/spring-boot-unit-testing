package com.luv2code.test;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes= MvcTestingExampleApplication.class)
public class MockAnnotationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent studentOne;

    @Autowired
    StudentGrades studentGrades;

    @Mock //or @MockBean
    private ApplicationDao applicationDao;

    @InjectMocks //if used @MockBean for dao class then here we can use @Autowired in place of @InjectMocks
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach(){
        studentOne.setFirstname("Abc");
        studentOne.setLastname("Kumar");
        studentOne.setEmailAddress("abc@gmail.com");
        studentOne.setStudentGrades(studentGrades);
    }

    @DisplayName("When and Verify")
    @Test
    public void assertEqualsTestAddGrades(){
        //setting result in database for math result as 100.00
        when(applicationDao
                .addGradeResultsForSingleClass(studentGrades
                        .getMathGradeResults()))
                .thenReturn(100.00);

        //checking math result from service class
        assertEquals(100,applicationService
                .addGradeResultsForSingleClass(studentOne
                        .getStudentGrades().getMathGradeResults()));

        //Verify that DAO method was called
        verify(applicationDao,times(1)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
    }

    @DisplayName("Find Gpa")
    @Test
    public void assertEqualsTestFindGpa() {
        when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults()))
                .thenReturn(88.31);
        assertEquals(88.31, applicationService.findGradePointAverage(studentOne
                .getStudentGrades().getMathGradeResults()));
    }

    @DisplayName("Not Null")
    @Test
    public void testAssertNotNull() {
        when(applicationDao.checkNull(studentGrades.getMathGradeResults()))
                .thenReturn(true);
        assertNotNull(applicationService.checkNull(studentOne.getStudentGrades()
                .getMathGradeResults()), "Object should not be null");
    }

    @DisplayName("Throw Runtime Exception")
    @Test
    public void throwRuntimeError(){
        //retreive college student from application context
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
        //setup the expectation to throw up the exception
        doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);
        //perform assertion
        assertThrows(RuntimeException.class,()->{
           applicationService.checkNull(nullStudent);
        });

        verify(applicationDao, times(1)).checkNull(nullStudent);
    }

    @DisplayName("Multiple Stubbing")
    @Test
    public void stubbingConsecutiveCalls() {
        CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");

        when(applicationDao.checkNull(nullStudent))
                .thenThrow(new RuntimeException())
                .thenReturn("Do not throw exception second time");

        assertThrows(RuntimeException.class, () -> {
            applicationService.checkNull(nullStudent);
        });

        assertEquals("Do not throw exception second time",
                applicationService.checkNull(nullStudent));

        verify(applicationDao, times(2)).checkNull(nullStudent);
    }








}
















