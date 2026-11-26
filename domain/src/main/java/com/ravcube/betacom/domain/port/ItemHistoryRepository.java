package com.ravcube.betacom.domain.port;

import com.ravcube.betacom.domain.spec.ItemHistoryEntry;
import java.util.List;
import java.util.UUID;

public interface ItemHistoryRepository {

    List<ItemHistoryEntry> findHistory(UUID itemId);
}
