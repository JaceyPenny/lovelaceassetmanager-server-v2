package io.lovelacetech.server.command.device;

import io.lovelacetech.server.command.BaseCommand;
import io.lovelacetech.server.repository.DeviceRepository;

public abstract class DeviceCommand<T extends DeviceCommand> implements BaseCommand {
  private DeviceRepository deviceRepository;

  public T setDeviceRepository(DeviceRepository deviceRepository) {
    this.deviceRepository = deviceRepository;
    return (T) this;
  }

  public DeviceRepository getDeviceRepository() {
    return deviceRepository;
  }

  @Override
  public boolean checkCommand() {
    return deviceRepository != null;
  }
}
