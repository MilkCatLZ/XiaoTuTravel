package shy.car.sdk.app.route

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.google.gson.Gson
import java.lang.reflect.Type

@Route(path = "/service/json")
class ObjectSerialisation : SerializationService {
    companion object {
        const val object1 = "Object1"
        const val object2 = "Object2"
    }

    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        return Gson().fromJson(input, clazz)
    }

    override fun init(context: Context?) {

    }

    override fun object2Json(instance: Any?): String {
        val result = Gson().toJson(instance)
        return result
    }

    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        return Gson().fromJson(input, clazz)
    }
}