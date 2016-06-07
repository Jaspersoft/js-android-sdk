/*
 * Copyright (C) 2016 TIBCO Jaspersoft Corporation. All rights reserved.
 * http://community.jaspersoft.com/project/mobile-sdk-android
 *
 * Unless you have purchased a commercial license agreement from TIBCO Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of TIBCO Jaspersoft Mobile SDK for Android.
 *
 * TIBCO Jaspersoft Mobile SDK is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TIBCO Jaspersoft Mobile SDK is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TIBCO Jaspersoft Mobile SDK for Android. If not, see
 * <http://www.gnu.org/licenses/lgpl>.
 */

package com.jaspersoft.android.sdk.service.report.schedule;

import com.jaspersoft.android.sdk.network.entity.schedule.JobCalendarTriggerEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobFormEntity;
import com.jaspersoft.android.sdk.network.entity.schedule.JobSimpleTriggerEntity;
import com.jaspersoft.android.sdk.service.data.schedule.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobTriggerMapperTest {

    @Mock
    JobCalendarTriggerMapper mCalendarTriggerMapper;
    @Mock
    JobSimpleTriggerMapper mSimpleTriggerMapper;
    @Mock
    JobNoneTriggerMapper mNoneTriggerMapper;

    private JobTriggerMapper mapperUnderTest;
    private JobFormEntity providedFormEntity, mappedFormEntity;
    private JobForm providedForm;

    private final JobFormFactory formFactory = new JobFormFactory();
    private JobForm.Builder formBuilder;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        formBuilder = formFactory.givenJobFormBuilderWithValues();
        mapperUnderTest = new JobTriggerMapper(mCalendarTriggerMapper, mSimpleTriggerMapper, mNoneTriggerMapper);
    }

    @Test
    public void should_map_simple_entity_trigger_to_form() throws Exception {
        givenEntityFormWithSimpleTrigger();

        whenMapsEntityToForm();

        verify(mSimpleTriggerMapper).mapEntityOnForm(formBuilder, providedFormEntity);
    }

    @Test
    public void should_map_calendar_entity_trigger_to_form() throws Exception {
        givenEntityFormWithCalendarTrigger();

        whenMapsEntityToForm();

        verify(mCalendarTriggerMapper).mapEntityOnForm(formBuilder, providedFormEntity);
    }

    @Test
    public void should_map_none_entity_trigger_to_form() throws Exception {
        givenEntityFormWithNoneTrigger();

        whenMapsEntityToForm();

        verify(mNoneTriggerMapper).mapEntityOnForm(formBuilder, providedFormEntity);
    }

    @Test
    public void should_map_simple_trigger_to_entity() throws Exception {
        givenFormWithSimpleTrigger();

        whenMapsFormToEntity();

        verify(mSimpleTriggerMapper).mapFormOnEntity(providedForm, mappedFormEntity);
    }

    @Test
    public void should_map_calendar_trigger_to_entity() throws Exception {
        givenFormWithCalendarTrigger();

        whenMapsFormToEntity();

        verify(mCalendarTriggerMapper).mapFormOnEntity(providedForm, mappedFormEntity);
    }

    @Test
    public void should_map_none_trigger_to_entity() throws Exception {
        givenFormWithNoneTrigger();

        whenMapsFormToEntity();

        verify(mNoneTriggerMapper).mapFormOnEntity(providedForm, mappedFormEntity);
    }

    private void givenEntityFormWithSimpleTrigger() {
        JobSimpleTriggerEntity triggerEntity = new JobSimpleTriggerEntity();
        triggerEntity.setRecurrenceInterval(1);
        triggerEntity.setRecurrenceIntervalUnit("DAY");
        providedFormEntity = new JobFormEntity();
        providedFormEntity.setSimpleTrigger(triggerEntity);
    }

    private void givenEntityFormWithCalendarTrigger() {
        JobCalendarTriggerEntity triggerEntity = new JobCalendarTriggerEntity();
        providedFormEntity = new JobFormEntity();
        providedFormEntity.setCalendarTrigger(triggerEntity);
    }

    private void givenEntityFormWithNoneTrigger() {
        JobSimpleTriggerEntity triggerEntity = new JobSimpleTriggerEntity();
        providedFormEntity = new JobFormEntity();
        providedFormEntity.setSimpleTrigger(triggerEntity);
    }

   private void givenFormWithSimpleTrigger() {
       IntervalRecurrence recurrence = new IntervalRecurrence.Builder()
               .withInterval(1)
               .withUnit(RecurrenceIntervalUnit.DAY)
               .build();
       Trigger trigger = new Trigger.Builder()
               .withRecurrence(recurrence)
               .build();
       JobForm.Builder builder = formFactory.givenJobFormBuilderWithValues();
       providedForm = builder.withTrigger(trigger).build();
   }

    private void givenFormWithCalendarTrigger() {
        CalendarRecurrence recurrence = new CalendarRecurrence.Builder()
                .withAllMonths()
                .build();
        Trigger trigger = new Trigger.Builder()
                .withRecurrence(recurrence)
                .build();
        JobForm.Builder builder = formFactory.givenJobFormBuilderWithValues();
        providedForm = builder.withTrigger(trigger).build();
    }

    private void givenFormWithNoneTrigger() {
        JobForm.Builder builder = formFactory.givenJobFormBuilderWithValues();
        providedForm = builder.build();
    }

    private void whenMapsEntityToForm() {
        mapperUnderTest.mapEntityOnForm(formBuilder, providedFormEntity);
    }

    private void whenMapsFormToEntity() {
        mapperUnderTest.mapFormOnEntity(providedForm, mappedFormEntity);
    }
}