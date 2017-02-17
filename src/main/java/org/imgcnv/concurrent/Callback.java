package org.imgcnv.concurrent;
/**Interface for callback function.
 *
 * @author Dmitry_Slepchenkov
 *
 */
public interface Callback {
/**Callback function.
 *
 * @param id     the task id.
 * @param url    the string to convert.
 */
    void callConvert(long id, String url);
}
