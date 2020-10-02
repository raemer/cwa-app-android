package de.rki.coronawarnapp.submission

import com.google.android.gms.nearby.exposurenotification.TemporaryExposureKey
import com.google.protobuf.ByteString
import de.rki.coronawarnapp.server.protocols.KeyExportFormat

class DefaultKeyConverter : KeyConverter {

    override fun toExternalFormat(key: TemporaryExposureKey, riskValue: Int) =
        KeyExportFormat.TemporaryExposureKey.newBuilder()
            .setKeyData(ByteString.readFrom(key.keyData.inputStream()))
            .setRollingStartIntervalNumber(key.rollingStartIntervalNumber)
            .setRollingPeriod(ROLLING_PERIOD)
            .setTransmissionRiskLevel(riskValue)
            .build()

    companion object {

        private const val ROLLING_PERIOD = 144
    }
}