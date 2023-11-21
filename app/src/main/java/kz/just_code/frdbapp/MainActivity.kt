package kz.just_code.frdbapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kz.just_code.frdbapp.databinding.ActivityMainBinding
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //val userDao = UserDao()
        val carDao = CarDao()

        binding.save.setOnClickListener {
            binding.progress.isVisible = true
            carDao.saveData(getCar()) {
                binding.progress.isVisible = false
            }
        }

        binding.get.setOnClickListener {
            carDao.getData()
        }

        binding.delete.setOnClickListener {
            carDao.removeData()
        }

        carDao.getDataLiveData.observe(this) {
            binding.user.text = it?.toString()
        }

        carDao.updateLiveData.observe(this) {
            binding.userUpdate.text = it?.toString()
        }
    }

    private fun getUser() = User(
        name = "Anton",
        lastName = "Ivanov",
        age = 24,
        gender = UserGender.M.name
    )

    private fun getCar() = Car(
        manufacture = "Tesla",
        model = "S",
        fuelType = FuelType.ELECTRO.name,
        engine = Engine(uid = UUID.randomUUID().toString(), power = 1024),
        electronicList = listOf(
            ElectronicBlock(name = "Engine block", uid = UUID.randomUUID().toString()),
            ElectronicBlock(name = "Climate block", uid = UUID.randomUUID().toString()),
            ElectronicBlock("asdfasd", uid = UUID.randomUUID().toString())
        )
    )
}

class UserDao : FRDBWrapper<User>() {
    override fun getTableName(): String = "User"

    override fun getClassType(): Class<User> = User::class.java
}

data class User(
    val name: String? = null,
    val lastName: String? = null,
    val age: Int? = null,
    val gender: String? = null
) {
    fun getUserGender(): UserGender = UserGender.values().firstOrNull {
        it.name == gender
    } ?: UserGender.W

    override fun toString(): String {
        return "name: $name, last name: $lastName, age: $age, gender: ${getUserGender().name}"
    }
}

enum class UserGender {
    M, W
}

class CarDao: FRDBWrapper<Car>() {
    override fun getTableName(): String = "Car"

    override fun getClassType(): Class<Car> = Car::class.java
}

data class Car(
    val manufacture: String? = null,
    val model: String? = null,
    val fuelType: String? = null,
    val engine: Engine? = null,
    val electronicList: List<ElectronicBlock>? = null
) {
    fun getCarFuelType(): FuelType = FuelType.values().firstOrNull {
        it.name == fuelType
    } ?: FuelType.ELECTRO

    override fun toString(): String {
        return "manufacture: $manufacture, model: $model, fuelType: ${getCarFuelType().name}, engine: ${engine.toString()}, electronicList: $electronicList"
    }
}

data class ElectronicBlock(
    val name: String? = null,
    val uid: String? = null
)

data class Engine(
    val uid: String? = null,
    val power: Int? = null
)

enum class FuelType{
    DISEL, BENZ, ELECTRO
}