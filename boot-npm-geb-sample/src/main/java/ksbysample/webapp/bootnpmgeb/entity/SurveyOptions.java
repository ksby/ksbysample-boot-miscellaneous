package ksbysample.webapp.bootnpmgeb.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

/**
 *
 */
@SuppressWarnings({"PMD.TooManyFields"})
@Entity
@Table(name = "SURVEY_OPTIONS")
public class SurveyOptions {

    /**
     *
     */
    @Id
    @Column(name = "GROUP_NAME")
    String groupName;

    /**
     *
     */
    @Id
    @Column(name = "ITEM_VALUE")
    String itemValue;

    /**
     *
     */
    @Column(name = "ITEM_NAME")
    String itemName;

    /**
     *
     */
    @Column(name = "ITEM_ORDER")
    Integer itemOrder;

    /**
     * Returns the groupName.
     *
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the groupName.
     *
     * @param groupName the groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Returns the itemValue.
     *
     * @return the itemValue
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     * Sets the itemValue.
     *
     * @param itemValue the itemValue
     */
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    /**
     * Returns the itemName.
     *
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Sets the itemName.
     *
     * @param itemName the itemName
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Returns the itemOrder.
     *
     * @return the itemOrder
     */
    public Integer getItemOrder() {
        return itemOrder;
    }

    /**
     * Sets the itemOrder.
     *
     * @param itemOrder the itemOrder
     */
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
}