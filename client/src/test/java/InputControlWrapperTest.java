import com.jaspersoft.android.sdk.client.ic.InputControlWrapper;
import com.jaspersoft.android.sdk.client.oxm.ResourceDescriptor;
import com.jaspersoft.android.sdk.client.oxm.ResourceProperty;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static junit.framework.Assert.*;

/**
 * @author Ivan Gadzhega
 * @version $Id$
 * @since 1.0
 */
public class InputControlWrapperTest {

    private final static String label = "Input Control";
    private final static String name = "inputControl";
    private final static String uriString = "/Samples/AllAccounts/inputControl";

    private ResourceDescriptor resourceDescriptor;


    @Before
    public void initResourceDescriptor() {
        resourceDescriptor = mock(ResourceDescriptor.class);
        when(resourceDescriptor.getLabel()).thenReturn(label);
        when(resourceDescriptor.getName()).thenReturn(name);
        when(resourceDescriptor.getUriString()).thenReturn(uriString);
    }

    @Test
    public void test_init() {
        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);
        assertEquals(label, wrapper.getLabel());
        assertEquals(name, wrapper.getName());
        assertEquals(uriString, wrapper.getUri());
    }

    @Test
    public void test_init_type() {
        ResourceProperty typeProperty = mock(ResourceProperty.class);
        when(typeProperty.getName()).thenReturn(ResourceDescriptor.PROP_INPUTCONTROL_TYPE);
        when(typeProperty.getValue()).thenReturn(Byte.toString(ResourceDescriptor.IC_TYPE_BOOLEAN));

        List<ResourceProperty> propertyList = new ArrayList<ResourceProperty>();
        propertyList.add(typeProperty);

        when(resourceDescriptor.getProperties()).thenReturn(propertyList);

        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

        assertEquals(ResourceDescriptor.IC_TYPE_BOOLEAN, wrapper.getType());
    }

    @Test
    public void test_init_isMandatory() {
        ResourceProperty typeProperty = mock(ResourceProperty.class);
        when(typeProperty.getName()).thenReturn(ResourceDescriptor.PROP_INPUTCONTROL_IS_MANDATORY);
        when(typeProperty.getValue()).thenReturn("true");

        List<ResourceProperty> propertyList = new ArrayList<ResourceProperty>();
        propertyList.add(typeProperty);

        when(resourceDescriptor.getProperties()).thenReturn(propertyList);

        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

        assertEquals(true, wrapper.isMandatory());
    }

    @Test
    public void test_init_isReadOnly() {
        ResourceProperty typeProperty = mock(ResourceProperty.class);
        when(typeProperty.getName()).thenReturn(ResourceDescriptor.PROP_INPUTCONTROL_IS_READONLY);
        when(typeProperty.getValue()).thenReturn("true");

        List<ResourceProperty> propertyList = new ArrayList<ResourceProperty>();
        propertyList.add(typeProperty);

        when(resourceDescriptor.getProperties()).thenReturn(propertyList);

        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

        assertEquals(true, wrapper.isReadOnly());
    }

    @Test
    public void test_init_isVisible() {
        ResourceProperty typeProperty = mock(ResourceProperty.class);
        when(typeProperty.getName()).thenReturn(ResourceDescriptor.PROP_INPUTCONTROL_IS_VISIBLE);
        when(typeProperty.getValue()).thenReturn("true");

        List<ResourceProperty> propertyList = new ArrayList<ResourceProperty>();
        propertyList.add(typeProperty);

        when(resourceDescriptor.getProperties()).thenReturn(propertyList);

        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

        assertEquals(true, wrapper.isVisible());
    }

    @Test
    public void test_init_query() {
        String query = "query";
        String dataSourceUri = "dataSourceUri";

        ResourceProperty queryProperty = mock(ResourceProperty.class);
        when(queryProperty.getValue()).thenReturn(query);

        ResourceDescriptor internalResource = mock(ResourceDescriptor.class);
        when(internalResource.getWsType()).thenReturn(ResourceDescriptor.WsType.query);
        when(internalResource.getDataSourceUri()).thenReturn(dataSourceUri);
        when(internalResource.getPropertyByName(ResourceDescriptor.PROP_QUERY)).thenReturn(queryProperty);

        List<ResourceDescriptor> internalResources = new ArrayList<ResourceDescriptor>();
        internalResources.add(internalResource);

        when(resourceDescriptor.getInternalResources()).thenReturn(internalResources);

        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

        assertEquals(query, wrapper.getQuery());
        assertEquals(dataSourceUri, wrapper.getDataSourceUri());
    }

    @Test
    public void test_init_singleValue_dataType() {
        ResourceProperty typeProperty = mock(ResourceProperty.class);
        when(typeProperty.getName()).thenReturn(ResourceDescriptor.PROP_INPUTCONTROL_TYPE);
        when(typeProperty.getValue()).thenReturn(Byte.toString(ResourceDescriptor.IC_TYPE_SINGLE_VALUE));

        List<ResourceProperty> propertyList = new ArrayList<ResourceProperty>();
        propertyList.add(typeProperty);

        when(resourceDescriptor.getProperties()).thenReturn(propertyList);

        Byte dataTypeValue = ResourceDescriptor.DT_TYPE_TEXT;

        ResourceProperty dataTypeProperty = mock(ResourceProperty.class);
        when(dataTypeProperty.getValue()).thenReturn(dataTypeValue.toString());

        ResourceDescriptor internalResource = mock(ResourceDescriptor.class);
        when(internalResource.getWsType()).thenReturn(ResourceDescriptor.WsType.dataType);
        when(internalResource.getPropertyByName(ResourceDescriptor.PROP_DATATYPE_TYPE)).thenReturn(dataTypeProperty);

        List<ResourceDescriptor> internalResources = new ArrayList<ResourceDescriptor>();
        internalResources.add(internalResource);

        when(resourceDescriptor.getInternalResources()).thenReturn(internalResources);

        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

        assertEquals(dataTypeValue.byteValue(), wrapper.getDataType());
    }

    @Test
    public void test_init_parameterDependencies() {
        Map<String, String> queries = new HashMap<String, String>();
        queries.put(
                "Standart Parameter",
                "select distinct billing_address_state, billing_address_country from accounts where "
                + "billing_address_country = $P{Country_multi_select} order by billing_address_country, billing_address_state"
        );
        queries.put(
                "Include Parameter",
                "select distinct billing_address_state, billing_address_country from accounts where "
                + "billing_address_country = $P!{Country_multi_select} order by billing_address_country, billing_address_state"
        );
        queries.put(
                "Dynamic Parameter",
                "select distinct billing_address_state, billing_address_country from accounts where "
                + "$X{IN, billing_address_country, Country_multi_select} order by billing_address_country, billing_address_state"
        );

        for (Map.Entry<String, String> query : queries.entrySet()) {
            ResourceProperty queryProperty = mock(ResourceProperty.class);
            when(queryProperty.getValue()).thenReturn(query.getValue());

            ResourceDescriptor internalResource = mock(ResourceDescriptor.class);
            when(internalResource.getWsType()).thenReturn(ResourceDescriptor.WsType.query);
            when(internalResource.getPropertyByName(ResourceDescriptor.PROP_QUERY)).thenReturn(queryProperty);

            List<ResourceDescriptor> internalResources = new ArrayList<ResourceDescriptor>();
            internalResources.add(internalResource);

            when(resourceDescriptor.getInternalResources()).thenReturn(internalResources);

            InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

            assertFalse(query.getKey(), wrapper.getParameterDependencies().isEmpty());
        }
    }


    @Test
    public void test_init_valuesList() {
        List<ResourceProperty> listOfValues = (List<ResourceProperty>) mock(List.class);

        ResourceProperty lovProperty = mock(ResourceProperty.class);
        when(lovProperty.getProperties()).thenReturn(listOfValues);

        ResourceDescriptor internalResource = mock(ResourceDescriptor.class);
        when(internalResource.getWsType()).thenReturn(ResourceDescriptor.WsType.lov);
        when(internalResource.getPropertyByName(ResourceDescriptor.PROP_LOV)).thenReturn(lovProperty);;

        List<ResourceDescriptor> internalResources = new ArrayList<ResourceDescriptor>();
        internalResources.add(internalResource);

        ResourceProperty typeProperty = mock(ResourceProperty.class);
        when(typeProperty.getName()).thenReturn(ResourceDescriptor.PROP_INPUTCONTROL_TYPE);
        when(typeProperty.getValue()).thenReturn(Byte.toString(ResourceDescriptor.IC_TYPE_SINGLE_SELECT_LIST_OF_VALUES));

        List<ResourceProperty> propertyList = new ArrayList<ResourceProperty>();
        propertyList.add(typeProperty);

        when(resourceDescriptor.getInternalResources()).thenReturn(internalResources);
        when(resourceDescriptor.getProperties()).thenReturn(propertyList);

        InputControlWrapper wrapper = new InputControlWrapper(resourceDescriptor);

        assertEquals(listOfValues, wrapper.getListOfValues());
    }

}
